//
//  VideoSession.swift
//  OpenLive
//
//  Created by GongYuhua on 6/25/16.
//  Copyright © 2016 Agora. All rights reserved.
//

import UIKit
import AgoraRtcEngineKit

class VideoSession: NSObject {
    var uid: UInt = 0
    var hostingView: UIView!
    var canvas: AgoraRtcVideoCanvas!
    
    init(channel:String, uid: UInt) {
        self.uid = uid
        
        hostingView = UIView(frame: CGRect(x: 0, y: 0, width: 100, height: 100))
        hostingView.translatesAutoresizingMaskIntoConstraints = false
        
        canvas = AgoraRtcVideoCanvas()
        canvas.uid = uid
        canvas.channel = channel as NSString
        canvas.view = hostingView
        canvas.renderMode = .hidden
    }
}

extension VideoSession {
    static func localSession() -> VideoSession {
        return VideoSession(channel:"", uid: 0)
    }
}
