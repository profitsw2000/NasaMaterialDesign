package ru.profitsw2000.nasamaterialdesign.utils

import ru.profitsw2000.nasamaterialdesign.representation.PODServerResponseData
import ru.profitsw2000.nasamaterialdesign.representation.earth.EarthServerResponseData

//Получение из объекта класса EarthServerResponseData объект класса PODServerResponseData.
// Из класса EarthServerResponseData считывается одно из полей, содержащее строковую переменную с url адресом.
// Считанное значение записывается в соответствующее поле объекта типа PODServerResponseData,
// остальные поля записываются произвольным значением.
fun convertESRDToPODSRD(earthServerResponseData: EarthServerResponseData) : PODServerResponseData {
    return PODServerResponseData("copyright",
        "date",
        "explanation",
        "mediatype",
        "title",
        earthServerResponseData.url,
        earthServerResponseData.url
    )
}