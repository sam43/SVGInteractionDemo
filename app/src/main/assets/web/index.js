window.androidObj = function AndroidClass() {};
var selectedlist = [];

var svgBody = document.getElementById('div').innerHTML;
var node = '.selected {fill: lightblue;}';
var nodex = '.default{fill:#e9e9e9;}';
var svg = document.getElementsByTagName('svg')[0];

var inner = svg.getElementsByTagName('style')[0].innerHTML;
var addingValue = nodex + inner + node;
svg.getElementsByTagName('style')[0].innerHTML = addingValue;

// 
document.addEventListener("click", doSomething);

var svgOutput = document.getElementById("div").outerHTML;

//console.log("" + document.getElementsByTagName('svg')[0].id);

//var tableId = document.getElementById(document.getElementsByTagName('svg')[0].id);
//console.log("" + tableId.getElementsByClassName('st489')[0].id);

var query = '*[id^=Code_]';
var tablePathList = document.querySelectorAll(query);
var table;
for (table = 0; table < tablePathList.length; table++) {
    tablePathList[table].removeAttribute('style');
    if (tablePathList[table].classList.contains('state')) {
        document.getElementById(tablePathList[table].id).classList.add('default');
    }
}

function doSomething(e) {
    //alert("Hello! I am an alert box!! with " + f);
    if (e.target !== e.currentTarget) {
        var clickedItem = e.target.id;
        var itemName;
        var item;

        for (item = 0; item < tablePathList.length; item++) {
            if (clickedItem === tablePathList[item].id) {
                var clickedSvgPath = document.getElementById(clickedItem);
                clickedSvgPath.classList.toggle("selected");
                if (!selectedlist.includes(clickedItem)) {
                    itemName = e.target.querySelector('title').innerHTML;
                    selectedlist.push(clickedItem);
                } else {
                    var index = selectedlist.indexOf(clickedItem);
                    if (index > -1) {
                        selectedlist.splice(index, 1);
                    }
                }
            }
        }
        console.log("Hello " + clickedItem);
        window.androidObj.textToAndroid(itemName);
        document.getElementById('l_value').innerHTML = itemName;
    }
    e.stopPropagation();
}

function updateFromAndroid(message) {
    document.getElementById('l_value').innerHTML = message;
}