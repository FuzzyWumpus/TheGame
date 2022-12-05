package com.example.thegame

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.thegame.R.drawable.*


private const val Tag = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var buttons: List<ImageButton>
    private lateinit var cards: List<MemoryCard>
    private var indexOfSingleSelectedCard: Int? = null
    private var points = 0
    private var correctpairs = 0
    private lateinit var score: TextView
    private var name = ""
    private var clicked = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val possibleImages = mutableListOf(cloud, fireflower, luigi, mario, mushroom, star)
       possibleImages.shuffle()

        val images = mutableListOf(possibleImages.get(0), possibleImages.get(1),
            possibleImages.get(2),possibleImages.get(3))


        // Add each image twice so we can create pairs
       images.addAll(images)
        // Randomize the order of images
        images.shuffle()

      score = findViewById(R.id.textView)
           score.setText("Points: " + points)


        buttons = listOf(
            findViewById(R.id.imageButton),
            findViewById(R.id.imageButton2),
            findViewById(R.id.imageButton3),
            findViewById(R.id.imageButton4),
            findViewById(R.id.imageButton5),
            findViewById(R.id.imageButton6),
            findViewById(R.id.imageButton7),
            findViewById(R.id.imageButton8),

            )

        cards = buttons.indices.map { index ->
            MemoryCard(images[index])
        }

        findViewById<Button>(R.id.tryAgain).setOnClickListener {

            if(indexOfSingleSelectedCard == null)
            restoreCards()
            updateViews()
        }

        findViewById<Button>(R.id.newGame).setOnClickListener {
            finish()
            startActivity(getIntent())
            overridePendingTransition(0,0)
        }

        findViewById<Button>(R.id.exitGame).setOnClickListener {
            //This is where we add code to change to main menu
        }



        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                Log.i(TAG, "button clicked!!")
                // Update models
                updateModels(index)
                // Update the UI for the game
                updateViews()
            }
        }
    }

        private fun updateViews() {
            cards.forEachIndexed { index, card ->
                val button = buttons[index]
                if (card.isMatched) {
                    button.alpha = 0.1f
                }
                button.setImageResource(if (card.isFaceUp) card.identifier else R.drawable.cardback)
            }
        }

        private fun updateModels(position: Int) {
            val card = cards[position]
            // Error checking:
            if (card.isFaceUp) {

                Toast.makeText(this, "Nope", Toast.LENGTH_SHORT).show()
                return
            }
            if (indexOfSingleSelectedCard == null) {
                // 0 or 2 selected cards previously

                    restoreCards()


                indexOfSingleSelectedCard = position
            } else {
                // exactly 1 card was selected previously
                checkForMatch(indexOfSingleSelectedCard!!, position)
                indexOfSingleSelectedCard = null


            }
            card.isFaceUp = !card.isFaceUp



        }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false

            }
        }
    }




    private fun checkForMatch(position1: Int, position2: Int) {
        if (cards[position1].identifier == cards[position2].identifier) {
            Toast.makeText(this, "Nice Job!", Toast.LENGTH_SHORT).show()
            cards[position1].isMatched = true
            cards[position2].isMatched = true
            correctpairs++
            points += 2
            score.text = "Points: " + points

            if (correctpairs == 4) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Name for your score")

// Set up the input

// Set up the input
                val input = EditText(this)
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_CLASS_TEXT
                builder.setView(input)

// Set up the buttons

// Set up the buttons
                builder.setPositiveButton("Set Name",
                    DialogInterface.OnClickListener { dialog, which ->
                        name = input.text.toString()
                    })
                builder.setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

                builder.show()
            }
        }
        else
        if (points > 0)
            points--
        score.text = "Points: " + points

     }


    }


