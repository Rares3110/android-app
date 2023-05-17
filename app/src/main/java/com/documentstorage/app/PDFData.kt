package com.documentstorage.app

enum class FileType { Files, Cloud }
data class PDFData(val title: String, val logo: Int, val date: String, val type: FileType)
