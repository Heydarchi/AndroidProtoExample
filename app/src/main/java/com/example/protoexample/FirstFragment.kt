package com.example.protoexample

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.proto.CustomMessage
import com.example.proto.CustomMessage.OtherReceiver
import com.example.protoexample.databinding.FragmentFirstBinding
import java.io.File

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private val TAG = "ProtoBufApp"
    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.storeCustomMessage.setOnClickListener {
            saveCustomMessage(
                CustomMessage.newBuilder()
                    .setTitle("Hello")
                    .setId(1)
                    .setPriority(CustomMessage.Priority.URGENT)
                    .setDateTime(System.currentTimeMillis().toString())
                    .setSenderName("John Doe")
                    .addReceivers(OtherReceiver.newBuilder().setReceiverName("Jane Wiliams").build())
                    .build()
            )
        }

        binding.loadCustomMessage.setOnClickListener {
            val customMessage = loadCustomMessage()

            val byteArray = customMessage.toByteArray()
            val hexString = byteArray.joinToString(" ") { "%02X".format(it) }
            Log.d("TAG", "CustomMessage as ByteArray (hex): $hexString")

            Log.v(TAG, customMessage.toString())
        }

    }

    private fun saveCustomMessage(customMessage: CustomMessage){
        val byteArray = customMessage.toByteArray()
        val hexString = byteArray.joinToString(" ") { "%02X".format(it) }
        Log.d("TAG", "CustomMessage as ByteArray (hex): $hexString")

        Log.v(TAG,"Custom message saved: $customMessage")
        val file = File(context?.filesDir, "custom_message.tmp")
        val fileBinary = File(context?.filesDir, "custom_message_binary.tmp")

        // Write as string to file
        file.writeText(customMessage.toByteArray().toString(Charsets.UTF_8))

        // Write as binary stream to file
        fileBinary.writeBytes(customMessage.toByteArray())


    }

    private fun loadCustomMessage(): CustomMessage {
        // Load the custom message from a temp file

        //Read as a string and convert it to bytes then parse it
        val file = File(context?.filesDir, "custom_message.tmp")
        if (!file.exists()) {
            Log.v(TAG,"No custom message found")
            return CustomMessage.getDefaultInstance()
        }
        val bytes_1 = file.readText(Charsets.UTF_8).toByteArray()
        val customMessage_1 = CustomMessage.parseFrom(bytes_1)
        val byteArray = customMessage_1.toByteArray()
        val hexString = byteArray.joinToString(" ") { "%02X".format(it) }
        Log.d("TAG", "CustomMessage as ByteArray (hex): $hexString")
        Log.v(TAG, "customMessage_1: $customMessage_1}")


        //Read as binary, parse and return it
        val fileBinary = File(context?.filesDir, "custom_message_binary.tmp")
        if (!fileBinary.exists()) {
            Log.v(TAG,"No custom message found")
            return CustomMessage.getDefaultInstance()
        }
        val bytes = fileBinary.readBytes()
        val customMessage = CustomMessage.parseFrom(bytes)
        return customMessage
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}