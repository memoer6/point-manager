

var userId;
var baseURL = "http://localhost:8090/user/";

//Transaction object, implemented to add and edit transaction
function Transaction(url, points, date, description, id) {
    this.url = url;
    this.points = points;
    this.date = date;
    this.description = description;
    this.id = id;
    //data to send in the http request body
    this.data = function() { 
        return JSON.stringify({"value":this.points,
                        "date":this.date,
                        "description":this.description,
                        "id": this.id});
        
    };
}

//User object
function User(name, totalPoints, id) {
    this.id = id;
    this.name = name;
    this.totalPoints = totalPoints;
    //data to send in the http request body
    this.data = function() { 
        return JSON.stringify({"name":this.name,
                        "totalPoints":this.totalPoints
                        });
                        
    };
}

//USER OPERATIONS

//Add a new user
//Called from addUser button in the index.html file
function addUser() {  
        
    editUserDialog(new User("",0), callback);    
        
    function callback(userObj) {
        
        sendHttpRequest("POST", baseURL, userObj.data(), successUserCallback,
                            errorCallback);    
    }
   
}

//Edit an existing user
//Called from editUser link
function editUser(userElement) {
    
    var url = baseURL + userElement.id + "/update"; 
    var userObj = new User(userElement.name, userElement.totalPoints.toFixed(2));
    editUserDialog(userObj, callback);  
    
    function callback(userObj) {
            sendHttpRequest("POST", url, userObj.data(), successUserCallback,
                                errorCallback);    
    }
    
}

//Delete an existing user
//Called from deleteUser link
function deleteUser(userElement) {
    
    var url = baseURL + userElement.id;
    deleteConfirmationDialog(yes, no);
    
    function yes() {        
        sendHttpRequest("DELETE", url, undefined, 
                    deleteUserCallback, errorCallback);  
    }
    
    function no() {//do nothing        
    };
    
    
    function deleteUserCallback() {
        removeChilds(document.getElementById("transactionBody"));
        removeChilds(document.getElementById("userLinksDiv"));
        removeChilds(document.getElementById("pointsDiv"));
        userListRequest();           
    }
    
}

//Creates dialog to edit user
//Called from editUser function
function editUserDialog(userObj, callback) {    
     
    var dialog = document.getElementById("editUserDialog");  
    
    //name input    
    var namebox = document.getElementById("nameId");
    namebox.value = userObj.name; 
    
    //total points input
    var totalPointsbox = document.getElementById("totalPointsId");
    totalPointsbox.value = userObj.totalPoints;     
   
    dialog.show();
    
    document.getElementById("acceptUserButton").onclick = function() {
        
        userObj.name = namebox.value;        
        userObj.totalPoints = totalPointsbox.value;           
        dialog.open = false;        
        callback(userObj);     
    };
    
    document.getElementById("cancelUserButton").onclick = function() {
        dialog.open = false;  
    };    
}

//TRANSACTION OPERATIONS

//Add a new transaction
//Called from addTransaction buttons in the index.html file
function addTransaction(element) { 
    
    //make sure userId exist
    readUser(); 
    
    //if user has been selected from the menu
    if (userId !== "") {
        
        var url = baseURL + userId + "/transaction"; 
        var halfPoint = document.getElementById("halfPointButton");
        var fullPoint = document.getElementById("fullPointButton");
        var transactionObj;

        if (element === halfPoint) { 

            transactionObj = new Transaction(url, "0.5",new Date().yyyymmdd(),"8:30");
            sendHttpRequest("POST", transactionObj.url, transactionObj.data(),
                                successTransactionCallback, errorCallback); 
                                        
        } else if (element === fullPoint) {      

            transactionObj = new Transaction(url, "1.0",new Date().yyyymmdd(),
                                        "8:25 and 8:30");
            sendHttpRequest("POST", transactionObj.url, transactionObj.data(), 
                                successTransactionCallback, errorCallback); 

        } else  {              

            editTransactionDialog(new Transaction(url, "0", 
                                    new Date().yyyymmdd(), ""), dialogCallback);
        }
       
    }
    
    function dialogCallback(transactionObj) {
        sendHttpRequest("POST", transactionObj.url, transactionObj.data(),
                            successTransactionCallback, errorCallback);
    }
  
}

//Edit an existing transaction
//Called from editTransaction link in transaction list table
function editTransaction(transactionObj) {   
    
    editTransactionDialog(transactionObj, callback); 
    
    function callback(transactionObj) {
            sendHttpRequest("POST", transactionObj.url, transactionObj.data(), 
                                    successTransactionCallback, errorCallback);    
    }
}

//Delete an existing transaction
//Called from deleteTransaction link in transaction list table
function deleteTransaction(element) {
    
    //remove "delete" from id string
    var transactionId = element.id.substring(6); 
    var url = baseURL + userId + "/transaction/" + transactionId;
    
    //style when the row is selected
    selectRow("row" + transactionId, "value" + transactionId, 
              "date" + transactionId, "description" + transactionId);
              
    deleteConfirmationDialog(yes, no);          
              
    function no() {        
         //style when the row is deselected
        deselectRow("row" + transactionId, "value" + transactionId, 
                  "date" + transactionId, "description" + transactionId);
    }
    
    function yes() {        
       sendHttpRequest("DELETE", url, undefined, successTransactionCallback,
            errorCallback); 
    }   
    
}

//Creates dialog to edit transaction
//Called from editTransaction function
function editTransactionDialog(transactionObj, callback) {
    
    //highlight transaction row if editing an existing transaction 
    if (transactionObj.id) {
        //style when the row is selected
        selectRow("row" + transactionObj.id, "value" + transactionObj.id, 
              "date" + transactionObj.id, "description" + transactionObj.id);
    }
    
    var dialog = document.getElementById("editTransactionDialog");  
    
    //points input    
    var pointsbox = document.getElementById("pointsId");
    pointsbox.value = transactionObj.points; 
    
    //date input
    var datebox = document.getElementById("dateId");
    datebox.value = transactionObj.date; 
    
     //description input
    var descripbox = document.getElementById("descripId");
    descripbox.value = transactionObj.description;         
      
    dialog.show();
    
    document.getElementById("acceptTransactionButton").onclick = function() {
        
        transactionObj.points = pointsbox.value;        
        transactionObj.date = datebox.value;
        transactionObj.description = descripbox.value; 
                
        dialog.open = false;  
        
        //deselect transaction row if editing an existing transaction 
        if (transactionObj.id) {
             //style when the row is deselected              
            deselectRow("row" + transactionObj.id, "value" + transactionObj.id, 
                  "date" + transactionObj.id, "description" + transactionObj.id);
        }
        
        callback(transactionObj);
    };
    
    document.getElementById("cancelTransactionButton").onclick = function() {
        dialog.open = false;   
        
        //deselect transaction row if editing an existing transaction 
        if (transactionObj.id) {
             //style when the row is deselected              
            deselectRow("row" + transactionObj.id, "value" + transactionObj.id, 
                  "date" + transactionObj.id, "description" + transactionObj.id);
        }
    };
}


//Common functions for user and transaction operations

//Delete user or transaction
//Called from deleteUser and deleteTransaction functions
function deleteConfirmationDialog(yes, no) {
    
    var dialog = document.getElementById("confirmDeleteDialog");   
    dialog.open = true;    
    
    document.getElementById("deleteButton").onclick = function() {
        dialog.open = false;  
        yes();
    };
    
    document.getElementById("cancelDeleteButton").onclick = function() {
       dialog.open = false;   
       no();
    };
    
}

function errorMessageDialog(text) {
    
    var dialog = document.getElementById("errorDialog");  
    var errorMessage = document.getElementById("errorMessage");
    errorMessage.innerHTML = text;
    dialog.open = true;    
    
    document.getElementById("okButton").onclick = function() {
        dialog.open = false;  
        
    };   
    
    
}

//DISPLAY OPERATIONS

//Request data of the user selected in the menu
//Called from the selection menu in the index.html file
function readUser() { 
    
    userId = document.getElementById("userMenu").value;
        
    //if user has been selected. No selection = ""
    if (userId !== "") {
        var url = baseURL + userId + "/data";    
        sendHttpRequest("GET", url, undefined, successUserCallback, errorCallback);
    } else errorMessageDialog("Select the user first");
                     
}

//Create and provision fields to display user data
//Called from callback function after user and transaction operations
function provisionUserData(user) {
    
    //Points field
    var pointsDiv = document.getElementById("pointsDiv");
    removeChilds(pointsDiv);  
    
    var totalPoints = document.createTextNode(user.totalPoints.toFixed(2));    
    pointsDiv.appendChild(totalPoints);
    
    //Edit and delete links
    var userLinksDiv = document.getElementById("userLinksDiv");
    removeChilds(userLinksDiv);  
    
    var editUserLink = document.createElement("a");  
    editUserLink.id = "editUserLink";
    editUserLink.href = "javascript: void(0)";
    editUserLink.innerHTML = "Edit User";


    editUserLink.onclick = function() {
        editUser(user);
    }; 
    
    var deleteUserLink = document.createElement("a");     
    deleteUserLink.id = "deleteUserLink";
    deleteUserLink.href = "javascript: void(0)";
    deleteUserLink.innerHTML = "Delete User";
    
    deleteUserLink.onclick = function(){           
            deleteUser(user);               
    };
      
    userLinksDiv.appendChild(editUserLink);
    userLinksDiv.appendChild(deleteUserLink);
}

//Create and display transaction table
//Called from callback function after user and transaction operations
function createTransactionTable(transactionList) {
    
    var transactionTable = document.getElementById("transactionTable");
    var transactionBody = document.getElementById("transactionBody"); 
  
  
    //remove previous elements if applicable
    removeChilds(transactionBody);

    //for (var i in transactionList.reverse()) { 
    for (var i in transactionList) { 
        
      
        var row = document.createElement("tr");
        
        row.id = "row" + transactionList[i].id;
        /*
        row.onclick = function(){           
            this.style.backgroundColor=(this.style.backgroundColor==='orange')?
            ('transparent'):('orange');                    
                };
        */          

        var cellValue = document.createElement("td");
        cellValue.id = "value" + transactionList[i].id;
        cellValue.innerHTML = transactionList[i].value.toFixed(2); 
        cellValue.classList.add("pointsColumn");
        row.appendChild(cellValue);
        
        var cellGap = document.createElement("td"); 
        cellGap.classList.add("gapColumn");
        row.appendChild(cellGap);

        var cellDate = document.createElement("td");
        cellDate.id = "date" + transactionList[i].id;        
        cellDate.innerHTML = transactionList[i].date;
        row.appendChild(cellDate);

        var cellDescription = document.createElement("td");
        cellDescription.id = "description" + transactionList[i].id;
        cellDescription.innerHTML = transactionList[i].description;
        cellDescription.classList.add("descriptionColumn");
        row.appendChild(cellDescription);
        
        var cellAction = document.createElement("td");
        
        //edit link
        var editLink = document.createElement("a");       
        editLink.id = "edit" + i;
        editLink.href = "javascript: void(0)";
        editLink.innerHTML = "Edit";
          
    
        editLink.onclick = function(event) {             
            
            var i = event.target.id.substring(4);  
            var url = baseURL + userId + "/update/transaction/";
            transactionObj = new Transaction(url, transactionList[i].value ,
                                            transactionList[i].date,
                                            transactionList[i].description,
                                            transactionList[i].id);
            
            editTransaction(transactionObj);    
                
        };    
        
        cellAction.appendChild(editLink);        
        
        var separator = document.createTextNode(' | ');
        cellAction.appendChild(separator);
        
        //delete link
        var deleteLink = document.createElement("a");
        deleteLink.id = "delete" + transactionList[i].id;
        deleteLink.href = 'javascript: void(0)';
        
        deleteLink.onclick = function(){           
            deleteTransaction(this);               
        };       
        
        deleteLink.innerHTML = "Delete";
        cellAction.appendChild(deleteLink);
        
        row.appendChild(cellAction);
        transactionBody.appendChild(row);

    }

    transactionTable.appendChild(transactionBody);
    
}

//Request the list to be displayed in user selection drop down menu
//It's called without userNameSelected parameter when the application starts
// (body.onload) and in deleteUser operation -> The user drop down menu shows
// "no selection"
//It's called from successUserCallback function with userNameSelected parameter ->
//the user is selected in drop down menu
function userListRequest(userNameSelected) {
    
    sendHttpRequest("GET", baseURL, undefined, callback, errorCallback);
    
    function callback(response) {
        userSelectionMenu(response, userNameSelected);
    }
}

//Create the user selection menu
//Called from userListRequest function
function userSelectionMenu(response, userSelected) { 
    
    var userMenu = document.getElementById("userMenu");      
    removeChilds(userMenu);
    
    var noSelection = document.createElement("option");
    noSelection.text = "no selection";
    noSelection.value = "";
    noSelection.disabled = true;    
    userMenu.add(noSelection);
    
 
    for (var i in response)  {    
        
        var user = document.createElement("option");
        user.id = response[i].name;
        user.text = response[i].name;
        user.value = response[i].id;
        userMenu.add(user);           
       
    }
    
    if (userSelected) {
        document.getElementById(userSelected).selected = true;
    } else {
        noSelection.selected = true;
    }
} 

//highlight transaction row in editing and deleting transaction operations
function selectRow(rowId, valueId, dateId, descriptionId) {
    
    //style when the row is selected
    document.getElementById(rowId).classList.add("selectedRow");
    document.getElementById(valueId).classList.add("selectedRow");
    document.getElementById(dateId).classList.add("selectedRow");
    document.getElementById(descriptionId).classList.add("selectedRow");
}

//unhighlight transaction row in canceling editing and deleting transaction
//operations
function deselectRow(rowId, valueId, dateId, descriptionId) {
    
    //style when the row is deselected
    document.getElementById(rowId).classList.remove("selectedRow");
    document.getElementById(valueId).classList.remove("selectedRow");
    document.getElementById(dateId).classList.remove("selectedRow");
    document.getElementById(descriptionId).classList.remove("selectedRow");
}

//define date format (eg. 2016-05-23)
Date.prototype.yyyymmdd = function() {
   var yyyy = this.getFullYear().toString();
   var mm = (this.getMonth()+1).toString(); // getMonth() is zero-based
   var dd  = this.getDate().toString();
   return yyyy + "-" + (mm[1]?mm:"0"+mm[0]) + "-" + (dd[1]?dd:"0"+dd[0]); // padding
};

//remove existing childs of element
//used when refreshing user menu, user information and transaction table
function removeChilds(element) {
    while (element.firstChild) {
       element.removeChild(element.firstChild);
    }
}


//Callback function used after adding, editing and deleting transactions
function successTransactionCallback(response) {      
    
    provisionUserData(response);
    var transactionList = response.transactionList;
    createTransactionTable(transactionList);
}

//Callback function used after reading user data, and adding and editing user
function successUserCallback(response) {

    //refresh user selection menu
    userListRequest(response.name);
    successTransactionCallback(response);
}

function errorCallback(response) {
    
    errorMessageDialog(response.message.toString());
}
    


//COMMUNICATION TO SERVER

//sends http request to server
//states: 0: request not initialized 1: server connection established
//2: request received 3: processing request 4: request finished 
//and response is ready 
function sendHttpRequest(type, url, data, successTransactionCallback,
                    errorCallback) {
    
    var xhr = new XMLHttpRequest();
    
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            var response = JSON.parse(xhr.responseText); 
            if (xhr.status === 200) {
                if (successTransactionCallback) successTransactionCallback(response);
            } else {
                if (errorCallback) errorCallback(response);
            }  
        } 
    };
    
    xhr.open(type, url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(data);
    
}



