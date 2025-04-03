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

        binding.storeCustomMessage.setOnClickListener {
            Log.v(TAG, loadCustomMessage().toString())
        }

    }

    private fun saveCustomMessage(customMessage: CustomMessage){
        val file = File(context?.filesDir, "custom_message.tmp")
        file.writeText(customMessage.toByteArray().toString(Charsets.UTF_8))

        Log.v(TAG,"Custom message saved: $customMessage")
    }

    private fun loadCustomMessage(): CustomMessage {
        // Load the custom message from a temp file

        val file = File(context?.filesDir, "custom_message.tmp")
        if (!file.exists()) {
            Log.v(TAG,"No custom message found")
            return CustomMessage.getDefaultInstance()
        }
        val bytes = file.readText(Charsets.UTF_8).toByteArray()
        val customMessage = CustomMessage.parseFrom(bytes)
        Log.v(TAG,"Custom message loaded: $customMessage")
        return customMessage
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}