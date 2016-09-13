package com.serotonin.mango.vo;

import com.serotonin.mango.view.text.TextRenderer;

public class RenderVo {
	private int id;
	private int dataType;
	private TextRenderer textRenderer;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RenderVo(int id, int dataType, TextRenderer textRenderer, String name) {
		super();
		this.id = id;
		this.dataType = dataType;
		this.textRenderer = textRenderer;
		this.name = name;
	}

	public RenderVo() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public TextRenderer getTextRenderer() {
		return textRenderer;
	}

	public void setTextRenderer(TextRenderer textRenderer) {
		this.textRenderer = textRenderer;
	}
}
