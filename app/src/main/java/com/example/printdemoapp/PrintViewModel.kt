package com.example.printdemoapp

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.io.FileOutputStream

class PrintViewModel() : ViewModel() {

    /**
     * Text entered by the user
     */
    var printString by mutableStateOf("")
        private set

    /**
     * Saves the input text so it can be used later to print it
     *
     * @param input current print text
     */
    fun updatePrintString(input: String) {
        printString = input
    }


    /**
     * Converts the given content text into a PDF file and send it to print
     *
     * @param context app context
     * @param content text to be printed
     * @param jobName print job name
     */
    fun printStringToPrinter(context: Context, content: String, jobName: String) {
        // Create a PDF document
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = android.graphics.Paint()
        paint.textSize = 12f
        canvas.drawText(content, 50f, 50f, paint)
        pdfDocument.finishPage(page)

        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager

        // Create a print document adapter
        val printAdapter = object : PrintDocumentAdapter() {
            override fun onLayout(
                oldAttributes: PrintAttributes?,
                newAttributes: PrintAttributes,
                cancellationSignal: CancellationSignal?,
                callback: LayoutResultCallback,
                extras: Bundle?
            ) {
                // Create a PrintDocumentInfo
                val info = PrintDocumentInfo.Builder(jobName)
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .build()

                // Respond to the layout request
                callback.onLayoutFinished(info, true)
            }

            override fun onWrite(
                pages: Array<out PageRange>?,
                destination: ParcelFileDescriptor?,
                cancellationSignal: CancellationSignal?,
                callback: WriteResultCallback?
            ) {
                // Write the PDF content to the printer
                val output = FileOutputStream(destination?.fileDescriptor)
                pdfDocument.writeTo(output)
                output.close()
                callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
            }
        }

        printManager.print(jobName, printAdapter, null)
    }

}