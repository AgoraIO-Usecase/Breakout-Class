//
//  SubChannelController.swift
//  AgoraDualChannels
//
//  Created by ZhangJi on 2018/6/21.
//  Copyright © 2018 ZhangJi. All rights reserved.
//

import UIKit
import AgoraRtcEngineKit

@objc protocol SubRtcEngineDelegate: NSObjectProtocol {
    @objc optional func rtc_Channel( channel: AgoraRtcChannel, didJoinChannel channel: String, withUid uid: UInt, elapsed: Int)
    @objc optional func rtc_Channel( channel: AgoraRtcChannel, didJoinedOfUid uid: UInt, elapsed: Int)
    @objc optional func rtc_Channel( channel: AgoraRtcChannel, didLeaveChannelWith stats: AgoraChannelStats)
    @objc optional func rtc_Channel( channel: AgoraRtcChannel, didOfflineOfUid uid: UInt, reason: AgoraUserOfflineReason)
    @objc optional func rtc_Channel( channel: AgoraRtcChannel, didOccurWarning warningCode: AgoraWarningCode)
    @objc optional func rtc_Channel( channel: AgoraRtcChannel, didOccurError errorCode: AgoraErrorCode)
}

class SubRtcEngineController: NSObject {
    
    weak var delegate: SubRtcEngineDelegate?
    
    convenience init(delegate: SubRtcEngineDelegate) {
        self.init()
        self.delegate = delegate
    }
    
    override init() {
        super.init()
    }
}

extension SubRtcEngineController: AgoraRtcChannelDelegate {
    func rtcChannelDidJoin(_ rtcChannel: AgoraRtcChannel, withUid uid: UInt, elapsed: Int) {
        delegate?.rtc_Channel?(channel: rtcChannel, didJoinChannel: rtcChannel.getId()!, withUid: uid, elapsed: elapsed)
    }
    
//    func rtcEngine(_ engine: AgoraRtcEngineKit, didJoinChannel channel: String, withUid uid: UInt, elapsed: Int) {
//        delegate?.subRtcEngine?(engine, didJoinChannel: channel, withUid: uid, elapsed: elapsed)
//    }
    
    func rtcChannel(_ rtcChannel: AgoraRtcChannel, didJoinedOfUid uid: UInt, elapsed: Int) {
        delegate?.rtc_Channel?(channel: rtcChannel, didJoinedOfUid: uid, elapsed: elapsed)
    }
    
//    func rtcEngine(_ engine: AgoraRtcEngineKit, didJoinedOfUid uid: UInt, elapsed: Int) {
//        delegate?.subRtcEngine?(engine, didJoinedOfUid: uid, elapsed: elapsed)
//    }
    
    func rtcChannel(_ rtcChannel: AgoraRtcChannel, didOfflineOfUid uid: UInt, reason: AgoraUserOfflineReason) {
        delegate?.rtc_Channel?(channel: rtcChannel, didOfflineOfUid: uid, reason: reason)
    }
    
//    func rtcEngine(_ engine: AgoraRtcEngineKit, didOfflineOfUid uid: UInt, reason: AgoraUserOfflineReason) {
//        delegate?.subRtcEngine?(engine, didOfflineOfUid: uid, reason: reason)
//    }
    
    func rtcChannelDidLeave(_ rtcChannel: AgoraRtcChannel, with stats: AgoraChannelStats) {
        delegate?.rtc_Channel?(channel: rtcChannel, didLeaveChannelWith: stats)
    }
    
//    func rtcEngine(_ engine: AgoraRtcEngineKit, didLeaveChannelWith stats: AgoraChannelStats) {
//        delegate?.subRtcEngine?(engine, didLeaveChannelWith: stats)
//    }
    
    func rtcChannel(_ rtcChannel: AgoraRtcChannel, didOccurWarning warningCode: AgoraWarningCode) {
        delegate?.rtc_Channel?(channel: rtcChannel, didOccurWarning: warningCode)
    }
    
//    func rtcEngine(_ engine: AgoraRtcEngineKit, didOccurWarning warningCode: AgoraWarningCode) {
//        delegate?.subRtcEngine?(engine, didOccurWarning: warningCode)
//    }
    
    func rtcChannel(_ rtcChannel: AgoraRtcChannel, didOccurError errorCode: AgoraErrorCode) {
        delegate?.rtc_Channel?(channel: rtcChannel, didOccurError: errorCode)
    }
    
//    func rtcEngine(_ engine: AgoraRtcEngineKit, didOccurError errorCode: AgoraErrorCode) {
//        delegate?.subRtcEngine?(engine, didOccurError: errorCode)
//    }
}
