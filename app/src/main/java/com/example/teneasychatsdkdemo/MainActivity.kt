package com.example.teneasychatsdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.teneasychatsdk.databinding.ActivityMainBinding
import com.teneasy.sdk.ChatLib
import com.teneasy.sdk.TeneasySDKDelegate
import com.teneasyChat.api.common.CMessage
import com.teneasyChat.gateway.GGateway

class MainActivity : AppCompatActivity(), TeneasySDKDelegate {

    private lateinit var chatLib: ChatLib
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatLib = ChatLib("CCcQARgRIBwoxtTNgeQw.BL9S_YLEWQmWzD1NjYHaDM3dUa6UOqgwOORaC9l8WyWuEVgCbxgd67GXmlQJsm1R2aQUgFDDrvpDsq3CmWqVAA", "wss://csapi.xdev.stream/v1/gateway/h5?token=", 0)
        chatLib.listener = this
        chatLib.makeConnect()

        binding.btnSend.setOnClickListener {
            sendMsg()
        }
    }

    private fun sendMsg(){
        val sayHello = "你好！"
        //val msgItem = chatLib.composeALocalMessage(sayHello)
        //addMsgItem(msgItem)
        chatLib.sendMessage(sayHello, CMessage.MessageFormat.MSG_TEXT)
        val payloadId = chatLib.payloadId
        val sendingMsg = chatLib.sendingMessage

        //chatLib.sendMessage("/3/public/1/1695821236_29310.jpg", CMessage.MessageFormat.MSG_IMG)

        chatLib.sendMessage("1.mp3", CMessage.MessageFormat.MSG_VOICE)

        chatLib.sendMessage("1.mp4", CMessage.MessageFormat.MSG_VIDEO)

        chatLib.sendMessage("2.mp4", CMessage.MessageFormat.MSG_VIDEO, 564321055359893503)
    }

    override fun receivedMsg(msg: CMessage.Message) {
        if (msg.content != null) {
            binding.tvContent.append(msg.content.toString() + "\n")
        }else if (msg.video != null){
            binding.tvContent.append(msg.video.toString() + "\n")
        }
        println(msg)
    }

    override fun msgReceipt(msg: CMessage.Message, payloadId: Long, msgId: Long) {
        //println(msg)
        val suc = if (msgId == 0L) "发送失败" else "发送成功"
        println(payloadId.toString() + " " +suc)

        if (msg.content.toString() != "") {
            binding.tvContent.append(msg.content.toString() )
        }else if (msg.video.toString() != ""){
            binding.tvContent.append(msg.video.toString() )
        }else if (msg.audio.toString() != ""){
            binding.tvContent.append(msg.audio.toString() )
        }

        binding.tvContent.append(payloadId.toString() + " " +suc +"\n")
    }

    override fun systemMsg(msg: String) {
        //TODO("Not yet implemented")
        Log.i("MainAct systemMsg", msg)
    }

    //成功连接，并返回相关信息，例如workerId
    override fun connected(c: GGateway.SCHi) {
        val workerId = c.workerId
        Log.i("MainAct connected", "成功连接")
        binding.tvContent.append("成功连接\n")
    }

    override fun workChanged(msg: GGateway.SCWorkerChanged) {
        Log.i("MainAct connected", "已经更换客服")
    }

    override fun onDestroy() {
        super.onDestroy()
        chatLib.disConnect()
    }

}