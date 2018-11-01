map.setView([$lat, $lon], $zoom);
map.attributionControl.setPrefix(null);

var osmAttrib='Map data © <a href="https://openstreetmap.org">OpenStreetMap</a> contributors';
L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", { attribution: osmAttrib }).addTo(map);

var marker = L.marker([$lat, $lon]).addTo(map);
window.vaadinMarkers = new Map();
window.vaadinMarkers.set(domId, marker);