package com.blue.rchina.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.List;

/** 
 * ITEM的对应可序化队列属性
 *  */
@Table(name = "redStone_channel")
public class ChannelItem extends BaseDBData implements Serializable,Comparable<ChannelItem> {

	/*"title": "推荐",
			"channelType": 2,
			"outLink": "",
			"showIcon": "",
			"channelid": 75*/

	@Column(name = "title")
	private String title;

	@Column(name = "channelid",property = "UNIQUE")
	private int channelid;

	@Column(name = "outLink")
	private String outLink;

	@Column(name = "showIcon")
	private String showIcon;

	@Column(name = "channelType")
	private int channelType;

	private List<ChannelItem> sons;


	@Column(name = "selected")
	private int  selected;

	@Column(name = "sortCount")
	private int sortCount;

	@Column(name = "parent_id")
	private int parent_id;

	@Column(name = "parent_name")
	private String parent_name;

	public ChannelItem() {
	}

	public String getParent_name() {
		return parent_name;
	}

	public void setParent_name(String parent_name) {
		this.parent_name = parent_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getChannelid() {
		return channelid;
	}

	public void setChannelid(int channelid) {
		this.channelid = channelid;
	}

	public String getOutLink() {
		return outLink;
	}

	public void setOutLink(String outLink) {
		this.outLink = outLink;
	}

	public String getShowIcon() {
		return showIcon;
	}

	public void setShowIcon(String showIcon) {
		this.showIcon = showIcon;
	}

	public int getChannelType() {
		return channelType;
	}

	public void setChannelType(int channelType) {
		this.channelType = channelType;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}

	public int getSortCount() {
		return sortCount;
	}

	public void setSortCount(int sortCount) {
		this.sortCount = sortCount;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	public List<ChannelItem> getSons() {
		return sons;
	}

	public void setSons(List<ChannelItem> sons) {
		this.sons = sons;
	}

	@Override
	public int compareTo(ChannelItem o) {
		return this.getSortCount()-o.getSortCount();
	}

	@Override
	public String toString() {
		return "ChannelItem{" +
				"title='" + title + '\'' +
				", channelid=" + channelid +
				", selected=" + selected +
				", sortCount=" + sortCount +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ChannelItem that = (ChannelItem) o;

		if (channelid != that.channelid) return false;
		return title != null ? title.equals(that.title) : that.title == null;

	}

	@Override
	public int hashCode() {
		int result = title != null ? title.hashCode() : 0;
		result = 31 * result + channelid;
		return result;
	}
}