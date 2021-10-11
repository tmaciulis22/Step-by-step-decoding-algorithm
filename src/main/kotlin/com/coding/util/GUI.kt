package com.coding.util

import javafx.event.EventTarget
import tornadofx.*

// Extension function which generates textField for binary bits
// Arguments: value of text field, boolean value which shows if textField is editable, onChange event listener
// Returns: textField component
fun EventTarget.textFieldBit(
    value: Int,
    isEditable: Boolean = true,
    onChange: ((Int) -> Unit)? = null
) = textfield(value.toString()) {
        this.isEditable = isEditable
        filterInput {
            val newText = it.controlNewText
            newText.isInt() && (newText == "1" || newText == "0")
        }
        textProperty().addListener { _, _, new ->
            if (new == "" || !new.isInt()) return@addListener

            val newIntValue = new.toInt()
            if (newIntValue == 0 || newIntValue == 1)
                onChange?.invoke(new.toInt())
        }
    }

// Extensions function which replaces current View with another one
inline fun <reified T : UIComponent> View.nextView(
    params: Map<String, Any>? = null,
    transition: ViewTransition? = ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
) = replaceWith(find(T::class, scope, params), transition)
