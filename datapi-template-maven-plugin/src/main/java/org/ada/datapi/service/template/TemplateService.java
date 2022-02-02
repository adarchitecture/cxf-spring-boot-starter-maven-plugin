package org.ada.datapi.service.template;

import com.google.common.collect.Lists;
import org.ada.datapi.data.Setting;
import org.ada.datapi.data.TemplateType;
import org.ada.datapi.domain.*;
import org.ada.datapi.domain.template.FlattenedField;
import org.ada.datapi.service.ConfigurationService;
import org.ada.datapi.service.ServiceProvider;
import org.ada.datapi.service.UtilityService;
import org.ada.datapi.util.PathUtil;
import org.ada.datapi.util.TypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public abstract class TemplateService {

    protected final ConfigurationService configurationService;

    @SuppressWarnings("unused")
	private final UtilityService utilityService = ServiceProvider.getUtilityService();

    @Getter
    protected final TemplateType templateType;

    private Map<String, String> exampleValueMap = new HashMap<>();

    public TemplateService(TemplateType templateType, ConfigurationService configurationService) {
        this.templateType = templateType;
        this.configurationService = configurationService;

        this.exampleValueMap.put("int", "1");
        this.exampleValueMap.put("Integer", "1");
        this.exampleValueMap.put("long", "1L");
        this.exampleValueMap.put("Long", "1L");
        this.exampleValueMap.put("double", "1D");
        this.exampleValueMap.put("Double", "1D");
        this.exampleValueMap.put("float", "1F");
        this.exampleValueMap.put("Float", "1F");
        this.exampleValueMap.put("String", "\"val\"");
    }

    /**
     * Adds information not applicable to all types of rendering being done.
     *
     * @param model
     *          model to which to add information
     * @param fileInformation
     *          information about the file to generate
     * @return the fully-customized model object
     */
    protected abstract JtwigModel addCustomInformation(JtwigModel model, FileInformation fileInformation);

    /**
     * Gets the appropriate template with which to perform the rendering
     *
     * @return the Jtwig template
     */
    protected abstract JtwigTemplate getTemplate();

    /**
     * The entry point to render a template.
     * @param fileInformation
     * @return
     */
    public final String renderTemplate(FileInformation fileInformation) {
        JtwigTemplate template = getTemplate();

        JtwigModel model = JtwigModel.newModel();
        model = addCustomInformation(model, fileInformation);

        ByteArrayOutputStream inMemOut = new ByteArrayOutputStream();
        PrintStream inMemPrint = new PrintStream(inMemOut);

        template.render(model, inMemPrint);

        String fileContent = new String(inMemOut.toByteArray(), StandardCharsets.UTF_8);

        return formatFile(fileContent);
    }

    /**
     * Adds the unique {@link Dependency}s for the fields attached to
     * the file information provided
     *
     * @param model
     *          model to which to add the dependencies
     * @param fileInformation
     *          information about the file to generate
     * @return the modified model
     */
    protected JtwigModel addDependencies(JtwigModel model, FileInformation fileInformation) {
        List<Field> fields = new ArrayList<>(fileInformation.getFields());
        fields.add(fileInformation.getId());

        return addDependencies(model, fields);
    }

    /**
     * Adds the {@link Dependency} for the given field to the model provided
     *
     * @param model
     *          model to which to add the dependency
     * @param field
     *          field which contains the dependency to add
     * @return the modified model
     */
    protected JtwigModel addDependencies(JtwigModel model, Field field) {
        return addDependencies(model, Lists.newArrayList(field));
    }

    /**
     * Adds the unique {@link Dependency}s for the given fields to the model provided
     *
     * @param model
     *          model to which to add the dependencies
     * @param fields
     *          fields which contain the dependencies to add
     * @return the modified model
     */
    protected JtwigModel addDependencies(JtwigModel model, List<Field> fields) {
         List<Dependency> dependencies = fields.stream()
                 .map(Field::getTypeDeclaration)
                 .map(TypeDeclaration::getTypes)
                 .flatMap(List::stream)
                 .map(Type::getDependency)
                 .filter(Objects::nonNull)
                 .distinct()
                 .collect(Collectors.toList());

        return model.with("dependencies", dependencies);
    }

    /**
     * Adds the id field attached to the {@link FileInformation} as a {@link FlattenedField}
     * to the provided model.
     *
     * @param model
     *          model to which to add the id field
     * @param fileInformation
     *          information about the file to generate
     * @return the modified model
     */
    protected JtwigModel addId(JtwigModel model, FileInformation fileInformation) {
        FlattenedField idField = new FlattenedField(fileInformation.getId());
        idField.setExampleValue(this.getExampleValue(fileInformation.getId()));

        return model.with("idField", idField);
    }

    /**
     * Adds the fields attached to the {@link FileInformation} as {@link FlattenedField}s
     * to the provided model.
     *
     * @param model
     *          model to which to add the fields
     * @param fileInformation
     *          information about the file to generate
     * @return the modified model
     */
    protected JtwigModel addFields(JtwigModel model, FileInformation fileInformation) {
        List<FlattenedField> fields = fileInformation.getFields().stream()
                .map(FlattenedField::new)
                .collect(Collectors.toList());

        return model.with("fields", fields);
    }

    /**
     * Adds fields in a consistent manner using a {@link FlattenedField}
     *
     * @param model
     *          model to which to add the field
     * @param rootClassName
     *          base name of the class
     * @param templateType
     *          which type are we adding
     * @return the modified model
     */
    protected JtwigModel addTypeField(JtwigModel model, String rootClassName, TemplateType templateType) {
        String packageName = getCustomPackage(templateType.relPathSetting);
        String className = TypeUtil.normalizeTypeString(rootClassName + templateType.suffix);
        String varName = TypeUtil.normalizeVariableName(rootClassName) + templateType.suffix;

        return model.with(templateType.templateVarName, new FlattenedField(packageName, className, varName));
    }

    protected JtwigModel addBaseRoute(JtwigModel model, FileInformation fileInformation) {
        return model.with("baseRoute", StringUtils.uncapitalize(fileInformation.getClassName()));
    }

    /**
     * Formats a string using the best Java formatting standards to ensure the generated
     * file is of proper format.
     *
     * @param fileString
     *          content to format
     * @return the formatted file content
     */
    protected String formatFile(String fileString) {
        return fileString.replaceAll("(\r?\n){3,}", "\n\n").replaceAll("\t", "    ");
    }

    /**
     * Gets the full path in which to generate a file for a given setting.
     *
     * Concatenates the {@link Setting#APP_PATH}, the giving setting path,
     * and the {@link Setting#SUB_DIR_PATH} and joins them in a safe and consistent way.
     *
     * @param setting
     *          setting for which to get a path
     * @return the path to generate the given path
     */
    private String getCustomPackage(Setting setting) {
        String appPackage = configurationService.get(Setting.APP_PATH);
        String relGenPackage = configurationService.get(setting);
        String customGenPackage = configurationService.get(Setting.SUB_DIR_PATH);

        String path = PathUtil.joinPath(appPackage, relGenPackage, customGenPackage);

        return PathUtil.pathToPackage(path);
    }

    private String getExampleValue(Field field) {
        List<Type> types = field.getTypeDeclaration().getTypes();
        if (CollectionUtils.isEmpty(types) || types.size() > 1) {
            return "null";
        }
        return this.exampleValueMap.getOrDefault(types.get(0).getClassName(), "null");
    }

}
