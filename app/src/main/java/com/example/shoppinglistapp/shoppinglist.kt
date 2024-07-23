package com.example.shoppinglistapp

import android.graphics.drawable.Icon
import android.icu.text.CaseMap.Title
import android.inputmethodservice.Keyboard.Row
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class shoppingitem(
    val id:Int,
    var name:String,
    var quantity:Int,
    var isediting: Boolean = false
)


@Composable
fun shoppinglistapp(){
    var sItems by remember{ mutableStateOf(listOf<shoppingitem>()) }
    var showdialog by remember { mutableStateOf(false)}
    var itemname by remember { mutableStateOf("")}
    var itemquantity by remember { mutableStateOf("")}
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ){
        Button(
            onClick = {showdialog = true},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ){
            Text(text = "Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(sItems){
                item->
                if(item.isediting){
                    ShoppingItemEditor(item = item, onEditcomplete = {
                        editedName,editedquantity ->
                        sItems = sItems.map {it.copy(isediting = false)}
                        val editeditem = sItems.find { it.id == item.id }
                        editeditem?.let {
                            it.name = editedName
                            it.quantity = editedquantity
                        }
                    } )
                }
                else{
                    shoppinglistitem(item = item ,
                        onEditClick = {
                        //finding out which item we are editing
                        sItems = sItems.map { it.copy(isediting = it.id == item.id) }
                    },
                        onDeleteClick = {
                        sItems = sItems-item
                    } )
                }

            }
        }
    }
    if(showdialog){
        itemquantity ="1"
        AlertDialog(onDismissRequest = { showdialog = false },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Button(onClick = {
                        if(itemname.isNotBlank()){
                            val newitem = shoppingitem(
                                id = sItems.size+1,
                                quantity = itemquantity.toInt(),
                                name = itemname
                            )
                            sItems = sItems + newitem
                            showdialog = false
                            itemname = ""
                        }
                    }) {
                        Text(text = "Add")
                    }
                    Button(onClick = { showdialog = false }) {
                        Text(text = "Cancel")
                    }

                }
            },
            title = {Text(text = "Add Shopping Item")},
            text = {
                Column {
                    OutlinedTextField(value = itemname, onValueChange ={itemname = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp))

                    OutlinedTextField(value = itemquantity, onValueChange ={itemquantity = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp))
                }

            }
        )

    }
}
@Composable
fun ShoppingItemEditor(item: shoppingitem,onEditcomplete:(String,Int)->Unit){
    var editedname by remember { mutableStateOf(item.name) }
    var editedquantity by remember { mutableStateOf(item.quantity.toString()) }
    var isediting by remember { mutableStateOf(item.isediting) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly){
        Column{
            BasicTextField(value = editedname,
                onValueChange = { editedname = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )

            BasicTextField(value = editedquantity,
                onValueChange = { editedquantity = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }

        Button(onClick = {
            isediting = false
            onEditcomplete(editedname,editedquantity.toIntOrNull() ?: 1)
        }) {
            Text(text = "Save")
        }
    }
}


@Composable
fun shoppinglistitem(
    item:shoppingitem,
    onEditClick:() -> Unit,
    onDeleteClick:() -> Unit
){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color.Blue),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = item.name,modifier = Modifier.padding(8.dp))
        Text(text = "Qty : ${item.quantity}",modifier = Modifier.padding(8.dp))

        Row (modifier = Modifier.padding(8.dp)){
            IconButton(onClick = onEditClick) {
                androidx.compose.material3.Icon(imageVector = Icons.Default.Edit,contentDescription = null)
            }
            IconButton(onClick = onDeleteClick) {
                androidx.compose.material3.Icon(imageVector = Icons.Default.Delete,contentDescription = null)
            }
        }

    }

}