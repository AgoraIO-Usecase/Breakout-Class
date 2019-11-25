//
//  LogCell.swift
//  AgoraDualChannels
//
//  Created by ZhangJi on 2018/6/15.
//  Copyright © 2018 ZhangJi. All rights reserved.
//

import UIKit

enum LogType {
    case info
    case warning
    case error
}

enum ChannelType{
    case main
    case sub
}

let infoColor = UIColor(hex: 0x66CCFF, alpha: 0.6)
let warningColor = UIColor(hex: 0xFF9966, alpha: 0.6)
let errorColor = UIColor(hex: 0xFF3333, alpha: 0.6)

class LogCell: UITableViewCell {

    @IBOutlet weak var logLabel: UILabel!
    @IBOutlet weak var logContainView: UIView!
    
    func set(log: String, withType type: LogType, forChannel channel: ChannelType){
        switch type {
        case .info:
            self.logContainView.backgroundColor = infoColor
            logLabel.text = channel == .sub ? "subInfo: " + log : "info: " + log
        case .warning:
            self.logContainView.backgroundColor = warningColor
            logLabel.text = channel == .sub ? "subWarning: " + log : "warning: " + log
        case .error:
            self.logContainView.backgroundColor = errorColor
            logLabel.text = channel == .sub ? "subError: " + log : "error: " + log
        }
    }
}
