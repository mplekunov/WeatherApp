package com.application.weatherapp.model.graph

class TripleValuePoint(
    startPoint: ValuePoint,
    val midPoint: ValuePoint,
    endPoint: ValuePoint
) : TupleValuePoint(startPoint, endPoint)