// DialogConfig.cpp : implementation file
//

#include "stdafx.h"
#include "OpenLive.h"
#include "DialogConfig.h"
#include "afxdialogex.h"


// CDialogConfig dialog

IMPLEMENT_DYNAMIC(CDialogConfig, CDialogEx)

CDialogConfig::CDialogConfig(CWnd* pParent /*=NULL*/)
	: CDialogEx(CDialogConfig::IDD, pParent)
{

}

CDialogConfig::~CDialogConfig()
{
}

void CDialogConfig::DoDataExchange(CDataExchange* pDX)
{
	CDialogEx::DoDataExchange(pDX);
	DDX_Control(pDX, IDC_COMBO_NETLib, m_cmbNetLib);
}


BEGIN_MESSAGE_MAP(CDialogConfig, CDialogEx)
	ON_BN_CLICKED(IDOK, &CDialogConfig::OnBnClickedOk)
	ON_CBN_SELCHANGE(IDC_COMBO_NETLib, &CDialogConfig::OnSelchangeComboNetlib)
END_MESSAGE_MAP()


// CDialogConfig message handlers


void CDialogConfig::OnBnClickedOk()
{
	// TODO: Add your control notification handler code here
	CDialogEx::OnOK();
}


BOOL CDialogConfig::OnInitDialog()
{
	CDialogEx::OnInitDialog();

	m_cmbNetLib.InsertString(0, _T("libuv"));
	m_cmbNetLib.InsertString(1, _T("libevent"));
	m_cmbNetLib.SetCurSel(0);
	return TRUE;  // return TRUE unless you set the focus to a control
	// EXCEPTION: OCX Property Pages should return FALSE
}


void CDialogConfig::OnSelchangeComboNetlib()
{
	forceAlternativeNetworkEngine = !m_cmbNetLib.GetCurSel();
}
