
// OpenLive.h : main header file for the PROJECT_NAME application
//

#pragma once

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"		// main symbols


// COpenLiveApp:
// See COpenLiveApp.cpp for the implementation of this class
//

class COpenLiveApp : public CWinApp
{
public:
    COpenLiveApp();

// Overrides
public:
	virtual BOOL InitInstance();

// Implementation

	DECLARE_MESSAGE_MAP()
public:
	BOOL forceAlternativeNetworkEngine = TRUE;
};

extern COpenLiveApp theApp;