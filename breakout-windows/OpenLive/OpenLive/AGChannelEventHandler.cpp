#include "stdafx.h"
#include "AGChannelEventHandler.h"


CAGChannelEventHandler::CAGChannelEventHandler() {
}


CAGChannelEventHandler::~CAGChannelEventHandler() {
}

void CAGChannelEventHandler::SetMsgReceiver(HWND hWnd) {
	m_hMainWnd = hWnd;
}

void CAGChannelEventHandler::onJoinChannelSuccess(IChannel *channel, uid_t uid, int elapsed) {
  char szbuf[MAX_PATH] = {"\0"};
  sprintf_s(szbuf, "%s(%s,%u,%d)\n", __FUNCTION__, channel, elapsed);
  OutputDebugStringA(szbuf);

  LPAGE_JOINCHANNEL_SUCCESS lpData = new AGE_JOINCHANNEL_SUCCESS;

  int nChannelLen = strlen(channel->channelId()) + 1;
  lpData->channel = new char[nChannelLen];
  lpData->uid = uid;
  lpData->elapsed = elapsed;

  strcpy_s(lpData->channel, nChannelLen, channel->channelId());

  if (m_hMainWnd != NULL)
    ::PostMessage(m_hMainWnd, WM_MSGID(EID_JOINCHANNEL_SUCCESS), (WPARAM)lpData, 0);
}

void CAGChannelEventHandler::onRejoinChannelSuccess(IChannel *channel, uid_t uid, int elapsed) {
  LPAGE_REJOINCHANNEL_SUCCESS lpData = new AGE_REJOINCHANNEL_SUCCESS;

  int nChannelLen = strlen(channel->channelId()) + 1;
  lpData->channel = new char[nChannelLen];
  lpData->uid = uid;
  lpData->elapsed = elapsed;

  strcpy_s(lpData->channel, nChannelLen, channel->channelId());

  if (m_hMainWnd != NULL)
    ::PostMessage(m_hMainWnd, WM_MSGID(EID_REJOINCHANNEL_SUCCESS), (WPARAM)lpData, 0);
}

void CAGChannelEventHandler::onChannelWarning(IChannel *channel, int warn, const char *msg) {
  CString str;

  str = _T("onWarning");
}

void CAGChannelEventHandler::onChannelError(IChannel *channel, int err, const char *msg) {
  LPAGE_ERROR lpData = new AGE_ERROR;

  int nMsgLen = 0;

  // attention: the pointer of msg maybe NULL!!!
  if (msg != NULL) {
    nMsgLen = strlen(msg) + 1;
    lpData->msg = new char[nMsgLen];
    strcpy_s(lpData->msg, nMsgLen, msg);
  } else
    lpData->msg = NULL;

  lpData->err = err;



  if (m_hMainWnd != NULL)
    ::PostMessage(m_hMainWnd, WM_MSGID(EID_ERROR), (WPARAM)lpData, 0);
}

void CAGChannelEventHandler::onLeaveChannel(IChannel *channel, const RtcStats &stat) {
  char szbuf[MAX_PATH] = {'\0'};
  sprintf_s(szbuf, "%s()\n", __FUNCTION__);
  OutputDebugStringA(szbuf);

  LPAGE_LEAVE_CHANNEL lpData = new AGE_LEAVE_CHANNEL;

  memcpy(&lpData->rtcStat, &stat, sizeof(RtcStats));

  if (m_hMainWnd != NULL)
    ::PostMessage(m_hMainWnd, WM_MSGID(EID_LEAVE_CHANNEL), (WPARAM)lpData, 0);
}

void CAGChannelEventHandler::onUserJoined(IChannel *channel, uid_t uid, int elapsed) {
  char szbuf[MAX_PATH] = {"\0"};
  sprintf_s(szbuf, "%s(%u,%d)\n", __FUNCTION__, uid, elapsed);
  OutputDebugStringA(szbuf);

  LPAGE_USER_JOINED lpData = new AGE_USER_JOINED;

  int nChannelIdLen = strlen(channel->channelId()) + 1;
  lpData->channel = new char[nChannelIdLen];
  strcpy_s(lpData->channel, nChannelIdLen, channel->channelId());

  lpData->uid = uid;
  lpData->elapsed = elapsed;

  if (m_hMainWnd != NULL)
    ::PostMessage(m_hMainWnd, WM_MSGID(EID_USER_JOINED), (WPARAM)lpData, 0);
}

void CAGChannelEventHandler::onUserOffline(IChannel *channel, uid_t uid, USER_OFFLINE_REASON_TYPE reason) {
  char szbuf[MAX_PATH] = {'\0'};
  sprintf_s(szbuf, "%s(%u,%d)\n", __FUNCTION__, uid, reason);
  OutputDebugStringA(szbuf);

  LPAGE_USER_OFFLINE lpData = new AGE_USER_OFFLINE;

  lpData->uid = uid;
  lpData->reason = reason;

  if (m_hMainWnd != NULL)
    ::PostMessage(m_hMainWnd, WM_MSGID(EID_USER_OFFLINE), (WPARAM)lpData, 0);
}