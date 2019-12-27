window.androidObj = function AndroidClass(){};
console.log("chromium attached");
var node = '.info {fill: lightsteelblue;} .booked {fill: #DFDFDF;} .selected {fill: #C96B5D;}';
var nodex = '.default{fill:#B9B7B7;}';
var svg = document.getElementsByTagName('svg')[0];

var inner = svg.getElementsByTagName('style')[0].innerHTML;
var addingValue = nodex + inner + node;
svg.getElementsByTagName('style')[0].innerHTML = addingValue;
var query = '*[id^="table"]';
var tablePathList = document.querySelectorAll(query);
var table;
for (table = 0; table < tablePathList.length; table++) {
    tablePathList[table].removeAttribute('style');
    if (tablePathList[table].classList.contains('draggable')) {
        document.getElementById(tablePathList[table].id).classList.add('default');
    }
}
document.addEventListener("click", focusTable);
function focusTable(e) {
  if (e.target !== e.currentTarget) {
    var clickedItem = e.target.id;
    var item;
    for (item = 0; item < tablePathList.length; item++) {
        if (clickedItem === tablePathList[item].id && !bookedList.includes(clickedItem)) {
            var clickedSvgPath = document.getElementById(clickedItem);
            window.androidObj.focusTable(clickedItem);
        }
        else if(clickedItem === tablePathList[item].id) {
            window.androidObj.textToAndroid("booked")
        }
    }
    console.log("Hello " + clickedItem);
  }
  e.stopPropagation();
}

function unFocusTable(svgId) {
    var svgPath = document.getElementById(svgId);
    svgPath.classList.toggle("info");
}
function updateFromAndroid(str) {
    var item;
    var index = selectedlist.indexOf(str);
    var clickedSvgPath = document.getElementById(str);
    clickedSvgPath.classList.toggle("selected");
    if (index > -1) {
        selectedlist.splice(index, 1);
    }
    console.log('selected: '+selectedlist)
}

function bookedTables(value) {
    bookedList = value.split(",");
    bookedList.forEach(function(str) {
        console.log("values" + str);
        var clickedSvgPath = document.getElementById(str);
        clickedSvgPath.classList.toggle("booked");
        var index = [tablePathList].indexOf(str);
        if (index > -1) {
            [tablePathList].splice(index, 1);
        }
    });
}

function selectTable(svgId) {
    var clickedSvgPath = document.getElementById(svgId);
    clickedSvgPath.classList.toggle("selected");
    selectedlist.push(svgId);
    console.log('chromium select table was called with params: ' + svgId + " " + floorId);
}

function unSelectTable(svgId) {
    var clickedSvgPath = document.getElementById(svgId);
    var index = selectedlist.indexOf(svgId);
    if (index > -1) {
        selectedlist.splice(index, 1);
        clickedSvgPath.classList.toggle("selected");
    }
    console.log('chromium unselect table was called with params: ' + svgId + " " + floorId);
}

function setfloorId(floorId) {
    this.floorId = floorId;
}

