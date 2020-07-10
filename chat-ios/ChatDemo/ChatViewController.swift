//
//  ChatViewController.swift
//  ChatDemo
//
//  Created by zzy on 2019/1/10.
//  Copyright © 2019 pwc. All rights reserved.
//

import UIKit
import SwiftyJSON

class ChatViewController: UIViewController,UITableViewDataSource,UITableViewDelegate {
    @IBOutlet weak var tv_msg_list: UITableView!
    @IBOutlet weak var v_input: UIView!
    @IBOutlet weak var tf_input: UITextField!
    @IBAction func sendMessage(_ sender: Any) {
        if let msg = tf_input.text{
            let chatMessage:ChatMessage = ChatMessage()
            chatMessage.fromUserId = CacheManager.shared.getUserId()
            chatMessage.toUserId = contact!.getContactId()
            chatMessage.chatMessageType = contact!.type == 0 ? ChatMessageType.SINGLE : ChatMessageType.GROUP
            let textBody = TextBody()
            textBody.text = msg
            chatMessage.setBody(bodyData: textBody)
            chatClient.send(chatMessage)
            
            tf_input.text = ""
        }else{
            print("请输入聊天内容")
        }
    }
    
    var v_input_constraint:NSLayoutConstraint?
    var contact:Contact?
    var chatClient = ChatClient.shared
    var messageManager = MessageManager.shared
    
    deinit {
        messageManager.removeObserver(self, forKeyPath: "messageDic")
        NotificationCenter.default.removeObserver(self)
    }
    
    @objc func keyBoardChanged(_ notifcation: Notification){
        if(v_input_constraint == nil){
            let constraints = self.view.constraints
            for constraint in constraints{
                let attribute = constraint.firstAttribute
                if(attribute == .bottom){
                    v_input_constraint = constraint
                    break
                }
            }
        }
        if(notifcation.name == UITextField.keyboardWillShowNotification){
            let info = notifcation.userInfo
            let keyboardRect:CGRect = info?[UITextField.keyboardFrameEndUserInfoKey] as! CGRect
            if(v_input.frame.minY > keyboardRect.origin.y){
                v_input_constraint!.constant = v_input.frame.minY + v_input.frame.height - keyboardRect.origin.y
            }
        }else{
            v_input_constraint!.constant = 0
        }
        updateViewConstraints()
    }
    
    override func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey : Any]?, context: UnsafeMutableRawPointer?) {
        DispatchQueue.main.async {
            print("更新消息列表")
            self.tv_msg_list.reloadData()
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationItem.title = contact?.getContactName()
        tv_msg_list.dataSource = self
        tv_msg_list.delegate = self
        tv_msg_list.tableFooterView = UIView.init(frame: CGRect.zero)
        tv_msg_list.keyboardDismissMode = .onDrag
        tv_msg_list.separatorStyle = .none
        tf_input.returnKeyType = UIReturnKeyType.send
        
        // 添加观察
        messageManager.addObserver(self, forKeyPath: "messageDic", options: .new, context: nil)
        // 注册通知
        NotificationCenter.default.addObserver(self, selector: #selector(self.keyBoardChanged(_:)), name: UITextField.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(self.keyBoardChanged(_:)), name: UITextField.keyboardWillHideNotification, object: nil)
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 60
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        var count = 0
        if let array:Array<ChatMessage> = messageManager.getMessageList(contact!){
            count =  array.count
        }
        print("消息数量：\(count)")
        return count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let array:Array<ChatMessage> = messageManager.getMessageList(contact!)!
        let chatMessage = array[indexPath.row]
        let fromUserId:Int64 = chatMessage.fromUserId
        let isMe = fromUserId == CacheManager.shared.getUserId()
        // 根据发送者加载不同cell
        let identifier:String = isMe ? "cell2": "cell"
        let cell = tableView.dequeueReusableCell(withIdentifier: identifier,for: indexPath)
        let timeLabel:UILabel = cell.viewWithTag(1) as! UILabel
        let userNameLabel:UILabel = cell.viewWithTag(2) as! UILabel
        let msgLabel:UILabel = cell.viewWithTag(3) as! UILabel
        let headImageView:UIImageView = cell.viewWithTag(4) as! UIImageView
        
        timeLabel.text = chatMessage.getMsgTime()
        msgLabel.text = chatMessage.getMessageValue()
        let defaultImage = UIImage(named: "ic_chat")
        var headUrl = ""
        if(isMe){
            let userInfo = CacheManager.shared.userInfo
            headUrl = userInfo!.head!
            userNameLabel.text = userInfo?.name
        }else{
            // 获取发送者头像
            if(chatMessage.chatMessageType == ChatMessageType.SINGLE){
                let friend:Contact = ChatUserManager.shared.getContact(contactType: 0, contactId: fromUserId)!
                headUrl = friend.friend!.head ?? ""
                userNameLabel.text = friend.getContactName()
            }else{
                for friend in (contact?.group!.memberList)!{
                    if friend.id == fromUserId {
                        headUrl = friend.head!
                        userNameLabel.text = friend.name
                        break
                    }
                }
            }
        }
        print("用户head\(headUrl)")
        if(headUrl != ""){
            headImageView.sd_setImage(with: URL(string: ApiConfig.HOST + "/" + headUrl), placeholderImage: defaultImage)
        }else{
            headImageView.image = defaultImage
        }
        
        return cell;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: false)
    }
    
}
