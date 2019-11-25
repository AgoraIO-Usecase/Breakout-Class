#pragma once
using namespace agora::rtc;

class CAGChannelEventHandler : public IChannelEventHandler {
public:
  CAGChannelEventHandler();
  ~CAGChannelEventHandler();

  void SetMsgReceiver(HWND hWnd = NULL);
  HWND GetMsgReceiver() { return m_hMainWnd; };
  virtual void onChannelWarning(IChannel *rtcChannel, int warn, const char *msg);
  virtual void onChannelError(IChannel *rtcChannel, int err, const char *msg);
  virtual void onJoinChannelSuccess(IChannel *rtcChannel, uid_t uid, int elapsed);
  virtual void onRejoinChannelSuccess(IChannel *rtcChannel, uid_t uid, int elapsed);
  virtual void onLeaveChannel(IChannel *rtcChannel, const RtcStats &stats);
  virtual void onUserJoined(IChannel *rtcChannel, uid_t uid, int elapsed);
  virtual void onUserOffline(IChannel *rtcChannel, uid_t uid, USER_OFFLINE_REASON_TYPE reason);

private :
	HWND m_hMainWnd;
};
