window.org_vaadin_maps_AbstractMap = function() {

    this.onStateChange = function() {
        //read state
        var domId = this.getState().domId;
        var mapjs = this.getState().mapjs;

        var connector = this;

        if (!window.vaadinMaps || !window.vaadinMaps.get(domId)) {
            var map = L.map(domId)
            //evaluate map JS
            eval(mapjs)

            window.vaadinMaps = new Map();
            window.vaadinMaps.set(domId, map);
        }
    }

}