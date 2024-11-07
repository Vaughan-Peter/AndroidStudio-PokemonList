package com.codelab.basics

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codelab.basics.ui.theme.BasicsCodelabTheme
import com.codelab.basics.ui.theme.Blue
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Open the DB
        val dbtest = DBClass(this@MainActivity)

        //dbtest.deleteDataTable()
        dbtest.buildDB()
        Log.d("CodeLab_DB", "onCreate Section: ")

        // Then the real data
        setContent {
            BasicsCodelabTheme {
                MyApp(
                    modifier = Modifier.fillMaxSize()
                    // Get the data from the DB for display
                    , names = dbtest.findAll(),
                    db = dbtest,
                )
            }
        }
    }
}
fun fetchImage(url: String): ByteArray? {
    return try {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()

        val responseCode = connection.responseCode
        if (responseCode != HttpURLConnection.HTTP_OK) {
            Log.e("fetchImage", "HTPP Error Code: $responseCode")
            return null
        }

        val input: InputStream = connection.inputStream
        val bitmap: Bitmap? = BitmapFactory.decodeStream(input)

        if (bitmap == null){
            Log.e("fetchImage", "Bitmap is null")
            return null
        }

        ByteArrayOutputStream().use { stream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.toByteArray()
        }
    } catch(e: Exception) {
        Log.e("fetchImage", "Error fetching image")
        return null
    }
}

@Composable
fun MyApp(
    modifier: Modifier = Modifier,
    names: List<DataModel>,
    db: DBClass
) {
    val windowInfo = rememberWindowInfo()  // get size of this screen
    var index by remember { mutableIntStateOf(-1) } // which name to display
    var showMaster: Boolean = (index == -1) // fudge to force master list first, when compact

    Surface(modifier, color = MaterialTheme.colorScheme.background) {
        //column here for pokemon
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            Log.d("CodeLab_DB", "MyApp0: index = $index ")
            //place favourite pokemon here
            //val dbtest = DBClass(LocalContext.current)
            //val favourite = dbtest.findFavourite()
            //Log.d("Favourite", favourite.toString())
        }
        if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
            if (showMaster or ((index < 0) or (index >= names.size))) {      //Always Check endpoints!
                Log.d("CodeLab_DB", "MyApp1: index = $index firstTime = $showMaster")
                showMaster = false
                ShowPageMaster(names = names, db = db,
                    updateIndex = { index = it })
            } else {
                Log.d("CodeLab_DB", "MyApp2: $index ")
                ShowPageDetails(name = names[index],  // List starts at 0, DB records start at 1
                    index = index,               // use index for prev, next screen
                    updateIndex = { index = it })
            }
        } else {  // show master details side-by-side
            // force visible entry if index=-1
            if (index < 0) {
                index = 0
            }
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .fillMaxHeight()
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Blue)
                        .fillMaxHeight()
                ) {
                    ShowPageMaster(names = names, db = db,
                        updateIndex = { index = it })
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    ShowPageDetails(name = names[index],  // List starts at 0, DB records start at 1
                        index = index,               // use index for prev, next screen
                        updateIndex = { index = it })
                }
            }
        }
    }
}

@Composable
private fun ShowPageMaster(

    modifier: Modifier = Modifier,
    names: List<DataModel>,
    db: DBClass,
    updateIndex: (index: Int) -> Unit
) {
    val dbtest = DBClass(LocalContext.current)
    val favourite = dbtest.findFavourite()

    LazyColumn(
        modifier = modifier.padding(vertical = 4.dp)
        .background(Color.Blue)
    ) {
        itemsIndexed(items = names) { pos, name ->
            if(pos == (favourite-1)){
                ShowEachListItem(name = name, pos, db, updateIndex,)
                Log.d("CodeLab_DB", "Favorite Pokemon: $pos +1 is $name \n")
            }
            /*else{
                ShowEachListItem(name = name, pos, updateIndex)
                Log.d("CodeLab_DB", "Favorite Pokemon: $pos +1 is $name \n")
            }*/
            Log.d("CodeLab_DB", "Item at index $pos is $name")
            ShowEachListItem(name = name, pos, db, updateIndex);
        }
    }
}

@Composable
private fun ShowEachListItem(
    name: DataModel,
    pos: Int,
    db: DBClass,
    updateIndex: (index: Int) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(vertical = 7.dp, horizontal = 11.dp)
    ) {
        CardContent(name, pos, updateIndex, db)
        Log.d("CodeLab_DB", "Greeting: ")
    }
}

@Composable
private fun CardContent(
    name: DataModel,
    pos: Int,
    updateIndex: (index: Int) -> Unit,
    db: DBClass
) {
    val dbTest = DBClass(LocalContext.current)
    val Context =LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var imageState = remember { mutableStateOf(name.image_col)}
    Row(
        modifier = Modifier
            .padding(28.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Cyan
                ),
                onClick = {
                    updateIndex(pos)
                    //Log.d("CodeLab_DB", "What is clicked = ${name.toString()} ")
                    //dbTest.accessCount(name.access_count, name.id)
                    //name.access_count++;
                })
            { Text(text = "Details ${pos + 1}") }
            Text(
                text = name.getName(),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold

                )
            )
            if (expanded) {
                Text(
                    text = (name.toString()),  // Full toString of data
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                )
                Log.d("CodeLab_DB", "Expanded name = ${name.toString()} ")

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray,
                        contentColor = Color.Cyan
                    ),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(name.getLink))
                        Context.startActivity(intent)

                        CoroutineScope(Dispatchers.IO).launch {
                            val fetchImage =
                                fetchImage(name.getLink)
                            Log.v("PokeLink", name.getLink)

                            if (fetchImage != null) {
                                db.updateImage(name.name, fetchImage)
                                Log.v("Image", "Image saved to database")
                                name.image_col = db.findImage(name.name)
                                imageState.value = name.image_col
                            } else {
                                Log.v("Image", "Failed to save image")
                            }
                        }
                    }
                ) { Text(text = "Website") }
            }
            if (imageState.value != null) {
                imageState.value?.let { bytes ->
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    Image(
                        bitmap.asImageBitmap(),
                        contentDescription = "Pokemon Image",
                        modifier = Modifier.size(128.dp)
                    )
                } ?: Log.e("ImageDisplay", "Bitmap is null, not displaying image")
            } else {
                Log.e("ImageDisplay", "name.image is null, not displaying image")
            }
        }


        IconButton(onClick = {
            if(!expanded){
                Log.d("CodeLab_DB", "What is clicked = ${name.toString()} ")
                dbTest.accessCount(name.access_count, name.id)
                name.access_count++;
            }
            expanded = !expanded }) {

            Icon(
                imageVector = if (expanded) Filled.ExpandLess else Filled.ExpandMore,
                contentDescription = if (expanded) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                }
            )
        }
    }
}

@Composable
private fun ShowPageDetails(
    name: DataModel,
    updateIndex: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    index: Int
) {
    val windowInfo = rememberWindowInfo()  // sorta global, not good
    Column(
        modifier = modifier.fillMaxWidth(0.5f).background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    )
    {
        Text(name.toString())
        Log.d("CodeLab_DB", "ShowData: $name.toString()")

        if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
            Button(onClick = { updateIndex(-1) })
            { Text(text = "Master List") }
        }
        // need check for end of array
        Button(onClick = { updateIndex(index + 1) })
        { Text(text = "Next Slide") }
        if (index > 0) {
            Button(onClick = { updateIndex(index - 1) })
            { Text(text = "Previous Slide") }
        }
    }
}