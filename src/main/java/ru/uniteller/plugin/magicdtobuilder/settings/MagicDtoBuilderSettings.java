package ru.uniteller.plugin.magicdtobuilder.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "MagicDtoBuilderSettings",
        storages = {
                @Storage("/magicDtoBuilder.xml")
        }
)
public class MagicDtoBuilderSettings implements PersistentStateComponent<MagicDtoBuilderSettings> {

    /**
     * TODO set default: "".
     */
    private String signatureMethodMagicDtoBuilderCreate = "#M#C\\App\\Library\\DtoBuilder\\DtoBuilder.create";

    /**
     * Get instance of plugin settings
     *
     * @param project project instance
     * @return self
     */
    public static MagicDtoBuilderSettings getInstance(Project project) {
        return ServiceManager.getService(project, MagicDtoBuilderSettings.class);
    }

    @Nullable
    @Override
    public MagicDtoBuilderSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull MagicDtoBuilderSettings settings) {
        XmlSerializerUtil.copyBean(settings, this);
    }

    /**
     * Get signature for method magic dto builder create
     *
     * @return signature
     */
    public String getSignatureMethodMagicDtoBuilderCreate() {
        return signatureMethodMagicDtoBuilderCreate;
    }
}
