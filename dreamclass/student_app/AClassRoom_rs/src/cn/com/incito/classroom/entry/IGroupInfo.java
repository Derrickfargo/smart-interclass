package cn.com.incito.classroom.entry;

import cn.com.incito.classroom.R;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 小组信息
 * @author 陈正
 *
 */
public class IGroupInfo extends IBaseInfo implements Parcelable{
	
	//唯一标识
	private String groupId ;
	//小组名称
	private String groupName;
	//小组图标
	private int groupIcon = R.drawable.icon ;
	//小组暗号
	private String groupPassword;
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		parcel.writeString(groupId);
		parcel.writeString(groupName);
		parcel.writeInt(groupIcon);
		parcel.writeString(groupPassword);
	}
	
	
	public static final Creator<IGroupInfo> CREATOR = new Creator<IGroupInfo>() {
		
		@Override
		public IGroupInfo[] newArray(int arg0) {
			return new IGroupInfo[arg0];
		}
		
		@Override
		public IGroupInfo createFromParcel(Parcel parcel) {
			
			IGroupInfo groupInfo = new IGroupInfo();
			groupInfo.setGroupId(parcel.readString());
			groupInfo.setGroupName(parcel.readString());
			groupInfo.setGroupPassword(parcel.readString());
			
			return groupInfo;
		}
	};
	
	
	
	
	
	
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getGroupIcon() {
		return groupIcon;
	}
	public void setGroupIcon(int groupIcon) {
		this.groupIcon = groupIcon;
	}
	public String getGroupPassword() {
		return groupPassword;
	}
	public void setGroupPassword(String groupPassword) {
		this.groupPassword = groupPassword;
	}
	
	
	

}
