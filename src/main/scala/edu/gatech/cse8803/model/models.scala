package edu.gatech.cse8803.model

import java.util.Date

case class Note(patientID: String, date: Date, category: String, text: String)
