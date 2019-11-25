#pragma once
#include "afxwin.h"


// CDialogConfig dialog

class CDialogConfig : public CDialogEx
{
	DECLARE_DYNAMIC(CDialogConfig)

public:
	CDialogConfig(CWnd* pParent = NULL);   // standard constructor
	virtual ~CDialogConfig();

// Dialog Data
	enum { IDD = IDD_DIALOG_CONFIG };

protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support

	DECLARE_MESSAGE_MAP()
public:
	CComboBox m_cmbNetLib;
	afx_msg void OnBnClickedOk();
	virtual BOOL OnInitDialog();

	BOOL forceAlternativeNetworkEngine = TRUE;
	afx_msg void OnSelchangeComboNetlib();
};
