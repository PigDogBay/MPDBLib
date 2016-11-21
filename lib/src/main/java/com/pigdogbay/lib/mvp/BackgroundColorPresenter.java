package com.pigdogbay.lib.mvp;

public class BackgroundColorPresenter {

	private BackgroundColorModel _Model;
	private IBackgroundColorView _View;
	
	public void setView(IBackgroundColorView view)
	{
		_View = view;
	}
	public void setModel(BackgroundColorModel model)
	{
		_Model = model;
	}
	public BackgroundColorPresenter(IBackgroundColorView view,BackgroundColorModel model)
	{
		_View = view;
		_Model = model;
	}
	public void resetBackground(){
		_Model.resetBackgroundIndex();
		updateBackground();
	}

	public void updateBackground()
	{
		int bgId = _Model.getBackgroundId();
		_View.setBackgroundColor(bgId);
	}
}
