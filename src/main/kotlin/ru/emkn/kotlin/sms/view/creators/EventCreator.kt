package ru.emkn.kotlin.sms.view.creators

import ru.emkn.kotlin.sms.ObjectFields
import ru.emkn.kotlin.sms.controller.Editor
import ru.emkn.kotlin.sms.model.Event


class EventCreator : ItemCreator<Event>() {

    override val fields = listOf(
        ItemCreatorInputField("Название", ObjectFields.Name),
        ItemCreatorInputField("Дата", ObjectFields.Date)
    )

    override fun createAction(input: Map<ObjectFields, String>) {
        Editor.createEventFrom(input)
    }

}