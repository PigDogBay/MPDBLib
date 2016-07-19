package com.pigdogbay.lib.mvp;

public class BackgroundColorPresenter {

	private IBackgroundColorModel _Model;
	private IBackgroundColorView _View;
	
	public void setView(IBackgroundColorView view)
	{
		_View = view;
	}
	public void setModel(IBackgroundColorModel model)
	{
		_Model = model;
	}
	public BackgroundColorPresenter()
	{
	}
	public BackgroundColorPresenter(IBackgroundColorView view,IBackgroundColorModel model)
	{
		_View = view;
		_Model = model;
	}

	public void updateBackground()
	{
		int bgId = _Model.getBackgroundId();
		_View.setBackgroundColor(bgId);
	}
	public String getColorName()
	{
		return _Model.getColorName();
	}
}
