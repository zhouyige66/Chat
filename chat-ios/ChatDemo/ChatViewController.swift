//
//  ChatViewController.swift
//  ChatDemo
//
//  Created by zzy on 2019/1/10.
//  Copyright © 2019 pwc. All rights reserved.
//

import UIKit

class ChatViewController: UIViewController,UITableViewDataSource,UITableViewDelegate {
    @IBOutlet weak var tv_msg_list: UITableView!
    @IBOutlet weak var v_input: UIView!
    @IBOutlet weak var tf_input: UITextField!
    @IBAction func sendMessage(_ sender: Any) {
        if let msg = tf_input.text{
            var message:ChatMessage<TextBody> = ChatMessage<TextBody>()
            message.fromUserId = CacheManager.shared.getUserId()
            message.toUserId = chatUser!.id
            message.id = UUID.init().uuidString
            message.type = ChatType.SINGLE.rawValue
            let textBody = TextBody()
            textBody.text = msg
            message.body = textBody
            chatClient.send(message)
            
            // 存储
            let jsonData:Data = (message.toJSONString()?.data(using: String.Encoding.utf8))!
            let json:Dictionary<String,Any> = try! JSONSerialization.jsonObject(with: jsonData, options: []) as! Dictionary<String, Any>
            MessageManager.shared.store(message: json)
            
            tf_input.text = ""
        }else{
            print("请输入聊天内容")
        }
    }
    
    var v_input_constraint:NSLayoutConstraint?
    var chatUser:User?
    var chatClient = ChatClient.shared
    var messageManager = MessageManager.shared
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.navigationItem.title = chatUser!.name
        
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
    
    override func observeValue(forKeyPath keyPath: String?, of object: Any?, change: [NSKeyValueChangeKey : Any]?, context: UnsafeMutableRawPointer?) {
        DispatchQueue.main.async {
            self.tv_msg_list.reloadData()
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 60
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        let dic = messageManager.messageDic
        if let array:Array<Any> = dic[chatUser!.id]{
            return array.count
        } else {
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let messageDic = messageManager.messageDic
        let array:Array<Any> = messageDic[chatUser!.id]!
        let dic:Dictionary<String,Any> = array[indexPath.row] as! Dictionary<String, Any>
        let fromUserId:String = dic["fromUserId"] as! String
        let isMe = fromUserId == CacheManager.shared.getUserId()
        let bodyJson:Dictionary<String, Any> = dic["body"] as! Dictionary<String, Any>
        let msg:String = bodyJson["text"] as! String
        
        // 根据发送者加载不同cell
        let identifier:String = isMe ? "cell2": "cell"
        let cell = tableView.dequeueReusableCell(withIdentifier: identifier,for: indexPath)
        let label_msg:UILabel = cell.viewWithTag(1) as! UILabel
        label_msg.text = msg
        
        return cell;
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: false)
    }
    
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
}
