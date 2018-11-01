package de.hgv.cirrus.webclient

import com.jarektoro.responsivelayout.ResponsiveColumn
import com.jarektoro.responsivelayout.ResponsiveLayout
import com.jarektoro.responsivelayout.ResponsiveRow
import com.vaadin.annotations.Push
import com.vaadin.annotations.Title
import com.vaadin.icons.VaadinIcons
import com.vaadin.server.ClassResource
import com.vaadin.server.Page
import com.vaadin.server.Sizeable
import com.vaadin.server.VaadinRequest
import com.vaadin.spring.annotation.SpringUI
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.CheckBoxGroup
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Image
import com.vaadin.ui.Label
import com.vaadin.ui.PopupView
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme
import de.hgv.cirrus.DataRepository
import de.hgv.cirrus.PictureRepository
import de.hgv.cirrus.model.DataType
import org.springframework.beans.factory.annotation.Autowired

@SpringUI
@Title("Wetterballon")
@Push
class MainView(@Autowired val dataRepository: DataRepository, @Autowired val pictureRepository: PictureRepository):
    UI() {

    private val show = mutableMapOf<DataType, Boolean>()
    private val columns = mutableMapOf<DataType, ResponsiveColumn>()

    override fun init(request: VaadinRequest) {
        setSizeFull()
        Page.getCurrent()
            .styles.add(".bg-dark-grey { background-color: #F0F0F0; } .myPopup { top: 50% !important; left: 50% !important; }")

        val responsiveLayout = ResponsiveLayout(ResponsiveLayout.ContainerType.FLUID)

        responsiveLayout.setScrollable(true)
        responsiveLayout.setSizeFull()

        val rootResponsiveRow = responsiveLayout.addRow()
        rootResponsiveRow.setHeight("100%")

        val menuCol = rootResponsiveRow.addColumn()
            .withDisplayRules(12, 12, 2, 2)
        menuCol.addStyleName("bg-dark-grey")

        val mainCol = rootResponsiveRow.addColumn()
            .withDisplayRules(12, 12, 10, 10)

        // Side Menu
        val sideMenu = SideMenu()
        menuCol.component = sideMenu

        // Icon
        val iconImage = Image(null, ClassResource("/static/icon.png"))
        iconImage.setHeight(150.0f, Sizeable.Unit.PIXELS)

        sideMenu.addColumn()
            .withDisplayRules(12, 12, 12, 12)
            .withComponent(iconImage, ResponsiveColumn.ColumnComponentAlignment.CENTER)
            .withVisibilityRules(true, true, true, true)
            .withGrow(false)

        // Show Diagram Buttons
        for (dataType in DataType.values().filterNot { it.isInternal() }) {
            val button = Button(dataType.toString()) { event ->
                show[dataType] = !show.getOrDefault(dataType, true)
                event.button.icon = if (show[dataType] == true) VaadinIcons.CHECK else VaadinIcons.CLOSE
                changeVisibility(dataType)
            }
            button.icon = VaadinIcons.CHECK
            button.styleName = ValoTheme.BUTTON_BORDERLESS

            sideMenu.addColumn()
                .withDisplayRules(12, 12, 12, 12)
                .withComponent(button, ResponsiveColumn.ColumnComponentAlignment.CENTER)
                .setVisibilityRules(false, false, true, true)
        }

        // Show Diagram Popup for small devices
        val popupContent = VerticalLayout()
        val checkBoxGroup = CheckBoxGroup<DataType>("Anzeigen", DataType.values().filterNot { it.isInternal() })
        checkBoxGroup.select(*DataType.values().filterNot { it.isInternal() }.toTypedArray())
        checkBoxGroup.addSelectionListener { event ->
            event.addedSelection.forEach { show[it] = !show.getOrDefault(it, true); changeVisibility(it) }
            event.removedSelection.forEach { show[it] = !show.getOrDefault(it, true); changeVisibility(it) }
        }
        val okButton = Button("OK")
        popupContent.addComponents(checkBoxGroup, okButton)

        val settingsPopup = PopupView(null, popupContent)
        settingsPopup.styleName = "myPopup"
        okButton.addClickListener {
            settingsPopup.isPopupVisible = false
        }

        val settingsButton = Button("Einstellungen") { _ ->
            settingsPopup.isPopupVisible = true
        }
        settingsButton.icon = VaadinIcons.COG
        settingsButton.addStyleNames(ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_HUGE)

        sideMenu.addColumn()
            .withDisplayRules(12, 12, 12, 12)
            .withComponent(
                HorizontalLayout(settingsButton, settingsPopup),
                ResponsiveColumn.ColumnComponentAlignment.CENTER
            )
            .setVisibilityRules(true, true, false, false)

        // Main Content
        val mainSectionLayout = ResponsiveLayout()
        mainCol.component = mainSectionLayout

        // Title
        val title = Label("Wetterballon - Messergebnissse")
        title.styleName = ValoTheme.LABEL_HUGE
        title.setWidthUndefined()

        val titleResponsiveRow = mainSectionLayout.addRow()
            .withAlignment(Alignment.MIDDLE_CENTER).withMargin(true)
        titleResponsiveRow.addColumn()
            .withComponent(title, ResponsiveColumn.ColumnComponentAlignment.CENTER)
            .withDisplayRules(3, 3, 3, 3)

        // Add Containers for each data type
        val containerRow = mainSectionLayout.addRow().withAlignment(Alignment.MIDDLE_CENTER).withMargin(true)
        for (dataType in DataType.values().filterNot { it.isInternal() }) {
            val column = containerRow.addColumn()
                .withDisplayRules(12, 12, 4, 4)
                .withComponent(ContentView(dataType, dataRepository, pictureRepository))

            columns[dataType] = column
        }

        content = responsiveLayout
    }

    fun changeVisibility(dataType: DataType) {
        columns[dataType]?.isVisible = show[dataType] == true

        (columns[dataType]?.component as? ContentView)?.changeVisibility(show[dataType] == true)
    }

    class SideMenu: ResponsiveRow() {
        //  private ResponsiveRow row;

        init {

            // was able to create a side menu with the given parts
            // not part of responsiveLayout lib

            setMargin(true)
            setVerticalSpacing(true)
            setDefaultComponentAlignment(Alignment.MIDDLE_LEFT)
        }
    }
}