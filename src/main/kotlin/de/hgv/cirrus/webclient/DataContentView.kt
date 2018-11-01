package de.hgv.cirrus.webclient

import com.vaadin.server.Page
import com.vaadin.ui.CustomComponent
import de.hgv.cirrus.DataRepository
import de.hgv.cirrus.model.Data
import de.hgv.cirrus.model.DataType
import org.vaadin.highcharts.HighChart

class DataContentView(val type: DataType, val dataRepository: DataRepository) : CustomComponent(), Updateable<Data> {

    private lateinit var chart: HighChart

    init {
        Page.getCurrent().styles.add(".mychart { padding: 10px; }")

        setSizeFull()

        setupChart()

        UIs.add(Data::class, this)

        addDetachListener {
            UIs.remove(Data::class, this)
        }
    }

    private fun setupChart() {
        chart = HighChart()
        var json = DataContentView::class.java.getResourceAsStream("/chart.js").bufferedReader().readText()
        json = json.replace("\$title", type.toString())
        json = json.replace("\$data", loadData())

        chart.setHcjs(json)
        chart.setWidth("95%")
        chart.setHeight("95%")

        chart.addStyleName("mychart")

        compositionRoot = chart
    }

    override fun add(item: Data) {
        if (!ui.isAttached) return

        ui.access {
            if (item.type == type) {
                chart.manipulateChart("chart.series[0].addPoint([${item.timeMillis}, ${item.value}], true, false, false);")
            }
        }
    }

    private fun loadData(): String {
        val data = dataRepository.findByType(type)

        return data.joinToString(",", "[", "]") {
            "[${it.timeMillis}, ${it.value}]"
        }
    }

    override fun changeVisibility(visible: Boolean) {
        setupChart()
    }
}