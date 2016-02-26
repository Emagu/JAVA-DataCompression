package Demopackage;
import javax.swing.*;
import javax.swing.filechooser.*;  
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*; // for BufferedImage
import java.io.*;
import javax.imageio.*; // for ImageIO
import java.lang.*;
import java.math.*;
import java.net.*;
//for image Icon
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.filechooser.FileView;
class ImageE{
	private DataOutputStream out = null;
	private DataInputStream In = null;
	public int dataLength;//�x�}����
	/*
		�g�J�G�i����
		�ѦҦ�:http://wenku.baidu.com/view/56e36a787fd5360cba1adb49
	*/
	public void SetOut(File file){
		try{
			out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		}catch(IOException iox){
			System.out.println("error");
		}
	}
	public void WriteToFile(short data){
		try{
			out.writeShort(data);
		}catch(IOException iox){
			System.out.println("error");
		}
	}
	public void WriteToFile(short[] data){
		try{
			for(int i=0;i<data.length;i++)
				out.writeShort(data[i]);
		}catch(IOException iox){
			System.out.println("error");
		}
	}
	public void WriteToFile(byte data){
		try{
			out.writeByte(data);
		}catch(IOException iox){
			System.out.println("error");
		}
	}
	public void WriteToFile(int data){
		try{
			out.writeInt(data);
		}catch(IOException iox){
			System.out.println("error");
		}
	}
	public void CloseDataOut(){
		try{
			out.close();
			out = null;
		}catch(IOException iox){
			System.out.println("error");
		}
	}
	/*
		Ū���G�i����
		�ѦҦ�:http://wenku.baidu.com/view/56e36a787fd5360cba1adb49
	*/
	public void SetIn(File file){
		try{
			In = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		}catch(IOException iox){
			System.out.println("error");
		}
	}
	public int ReadFromFileAsInt(){
		try{
			return In.readInt();
		}catch(IOException iox){
			System.out.println("error");
			return 0;
		}
	}
	public short ReadFromFileAsShort(){
		try{
			return In.readShort();
		}catch(IOException iox){
			System.out.println("error");
			return 0;
		}
	}
	public short ReadFromFileAsByte(){
		try{
			return In.readByte();
		}catch(IOException iox){
			System.out.println("error");
			return 0;
		}
	}
	public void FileDel(File file){
		try{
			if(file.delete()){
				System.out.println(file.getName() + " is deleted!");
			}else{
				System.out.println("Delete operation is failed.");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public short[][][] GetRGB(File file){//O(n*n)
		try{
			BufferedImage image = ImageIO.read(file);  
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int width = image.getWidth();  
			int height = image.getHeight();
			short[][][] imgRGB = new short[3][height][width];//�s��Ϥ�RGB
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int temp = image.getRGB(x, y);
					imgRGB[0][y][x] = (short)((temp & 0xFF0000) >> 16);
					imgRGB[1][y][x] = (short)((temp & 0xFF00) >> 8);
					imgRGB[2][y][x] = (short)(temp & 0xFF);
				}
			}
			return imgRGB;
		}catch(IOException e){
			System.out.println("1");
			return new short[1][1][1];
		}
	}
	public void SetRGB(short[][][] imgRGB,File file){//O(n*n)
		int width = imgRGB[0][0].length;  
		int height = imgRGB[0].length;
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
		for (int r = 0; r < height; r++) { 
			for (int c = 0; c < width; c++) { 
				int d = 0xff000000;
				d = d | ((imgRGB[0][r][c] | 0x00) << 16);
				d = d | ((imgRGB[1][r][c] | 0x00) << 8);
				d = d | (imgRGB[2][r][c] | 0x00);
				bi.setRGB(c, r, d);
			} 
		}
		try{
			int endIndex = file.getName().lastIndexOf(46);
			String Type = file.getName().substring(endIndex+1);
			System.out.println("�ɦW:"+file.getName()+"����:"+Type);
			ImageIO.write(bi, Type, file);
			System.out.println("�g�ɧ���!");
		}catch (IOException ex) { 
			ex.printStackTrace(); 
		}
	}
	public byte OverPowON(short[][] data){//O(n*n)
		short temp = 0;
		for(int i=0;i<dataLength;i++){
			for(int j=0;j<dataLength;j++){
				temp += data[i][j];
			}
		}
		byte dataOut = (byte)(Math.round(temp/(dataLength*dataLength))-128);
		return dataOut;
	}
	public short[][] OverPowOFF(short data){//O(n*n)
		short[][] dataOut = new short[dataLength][dataLength];
		for(int i=0;i<dataLength;i++){
			for(int j=0;j<dataLength;j++){
				dataOut[i][j]=(short)(data+128);
			}
		}
		return dataOut;
	}
}
class MyThread extends Thread{
	boolean ShotDown = false;
	public void callShowDown(){
		ShotDown = true;
	}
	protected static void Refresh(JLabel WorkState,JProgressBar progressbar,int progressNum){
		WorkState.setText("����i��:"+progressNum+"%");
		WorkState.setForeground(Color.blue);
		progressbar.setValue(progressNum);
	}
}
class CLZip extends MyThread{
	File InFile;
	File OutFile;
	JLabel WorkState;
	JProgressBar progressbar;
	JFrame demo;
	private int encodeClass;
	public CLZip(File InFile,File OutFile,JLabel WorkState,JProgressBar progressbar,JFrame demo,int encodeClass){
		this.InFile = InFile;
		this.OutFile = OutFile;
		this.WorkState = WorkState;
		this.progressbar = progressbar;
		this.demo = demo;
		this.encodeClass = encodeClass;
	}
	public void run() { 
        ImageE ImgTool = new ImageE();
		ImgTool.SetOut(OutFile);
		System.out.println("CL");
		System.out.println("�}�lŪ��:");
		System.out.println("�ɮ׸�T:");
		System.out.println("���|:"+InFile.getPath());
		short[][][] data = ImgTool.GetRGB(InFile);
		//��s�i�ױ�
			int progressNum = 5;
			Refresh(WorkState,progressbar,progressNum);
		int height = data[0].length;
		int width = data[0][0].length;
		System.out.println("X�b:"+width);
		System.out.println("Y�b:"+height);
		System.out.println("���Y����:"+encodeClass);
		System.out.println("Ū�ɦ��\!");
		System.out.println("�إߥ~�s�ɮ�!");
		System.out.println("�x�s�ɮ׳]�w!");
		ImgTool.WriteToFile((short)height);
		ImgTool.WriteToFile((short)width);
		ImgTool.WriteToFile((byte)encodeClass);
		byte[] Temp = new byte[width*height*3];
		//CodeBook init
			HuffmenNode[] CodeBook = new HuffmenNode[encodeClass];//�i���ܼƶq
			for(int i=0;i<encodeClass;i++) CodeBook[i] = new HuffmenNode((short)i);
		//��s�i�ױ�
			progressNum += 5;
			Refresh(WorkState,progressbar,progressNum);
		//�}�l�s�X
			System.out.println("�إ߶��j�}�C!");
			int[] ClassRange = new int [encodeClass+1];
			ClassRange[encodeClass] = 256;
			for(int i = 0;i < encodeClass;i++){
				ClassRange[i] = (255/encodeClass)*i;
				System.out.println(ClassRange[i]);
			}
			System.out.println("�}�l�s�X!");
			int progressRange = (int)Math.round(height*width/7);
			for(int i=0;i<3;i++){
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						for(int j=0;j<encodeClass;j++){
							if(ShotDown){
								ImgTool.CloseDataOut();
								ImgTool.FileDel(OutFile);
								return;
							}
							//��s�i�ױ�
							if((i*height*width+y*height+x)%progressRange==0){
								progressNum++;
								Refresh(WorkState,progressbar,progressNum);
							}
							if((data[i][y][x]>=ClassRange[j])&&(data[i][y][x]<ClassRange[j+1])){
								int index = i*height*width+y*width+x+3;
									CodeBook[j].addFrq();//�W�[�W�v
									Temp[i*height*width+y*width+x] = (byte)j;
								break;
							}
						}
					}
				}
			}
		System.out.println("�s�X����!");
		//�}�l�g��
			Huffmen OutHuff = new Huffmen(CodeBook);
			int[] Out = OutHuff.EncoderConnect(Temp,progressbar,WorkState);//�s�X���X
			//�g�ɭ��]�w��
			byte[] OutTemp = new byte[encodeClass*2];//�i���ܼƶq*2
			int OutNum = 0;
			for(int i=0;i<encodeClass;i++){
				if(CodeBook[i].getFrq()>0){
					OutTemp[OutNum++] = (byte)CodeBook[i].getValue();//Value
					OutTemp[OutNum++] = (byte)(CodeBook[i].getCode().length()-1);//Code���X��1
				}
			}
			ImgTool.WriteToFile((byte)OutNum);//���X��Huffmen Code
			for(int i=0;i<OutNum;i++){
				ImgTool.WriteToFile(OutTemp[i]);//Value
				ImgTool.WriteToFile(OutTemp[++i]);//Code���X��1
			}
			ImgTool.WriteToFile(Out.length);//���X��int ��Data
			//�g���e
			for(int i=0;i<Out.length;i++){
				ImgTool.WriteToFile(Out[i]);//DataCode
			}
		//�����g��
			ImgTool.CloseDataOut();
		WorkState.setText("����i��:100%");
		WorkState.setForeground(Color.blue);
		progressbar.setValue(100);
		JOptionPane.showMessageDialog(demo,"���Y����\n���Y��:"+((float)InFile.length()/(float)OutFile.length()),"�T��",JOptionPane.INFORMATION_MESSAGE); 
    }
}
class CLUnZip extends MyThread{
	File InFile;
	File OutFile;
	JLabel WorkState;
	JProgressBar progressbar;
	public CLUnZip(File InFile,File OutFile,JLabel WorkState,JProgressBar progressbar){
		this.InFile = InFile;
		this.OutFile = OutFile;
		this.WorkState = WorkState;
		this.progressbar = progressbar;
	}
	// override Thread's run()
	public void run() { 
        //�}�lŪ��
			ImageE ImgTool = new ImageE();
			ImgTool.SetIn(InFile);
			System.out.println("UNCL");
			System.out.println("Ū���ɮ׳]�w!");
			int height = (int)ImgTool.ReadFromFileAsShort();
			System.out.println("Y�b:"+height);
			int width = (int)ImgTool.ReadFromFileAsShort();
			System.out.println("X�b:"+width);
			int encodeClass = (int)ImgTool.ReadFromFileAsByte();
			System.out.println("���Y����:"+encodeClass);
			int InNum = (int)ImgTool.ReadFromFileAsByte();
			byte[] InTemp = new byte[InNum];//�i���ܼƶq*2
			int progressNum = 5;
			Refresh(WorkState,progressbar,progressNum);
			for(int i=0;i<InNum;i++){
				InTemp[i] = (byte)ImgTool.ReadFromFileAsByte();
			}
			int[] In = new int[ImgTool.ReadFromFileAsInt()];
		//���o�ɮפ��e
			for(int i=0;i<In.length;i++){
				In[i] = ImgTool.ReadFromFileAsInt();
			}
		//CodeBook init
			HuffmenNode[] CodeBook = new HuffmenNode[encodeClass];//�i���ܼƶq
			for(int i=0;i<encodeClass;i++) CodeBook[i] = new HuffmenNode((short)i);
			//�N�]�w��Ū�JCODEBOOK
			for(int i=0;i<InNum;i++) {
				String CodeTemp="";
				for(int j=0;j<InTemp[i+1];j++) CodeTemp+="1";
				CodeTemp+="0";
				CodeBook[InTemp[i++]].setCode(CodeTemp);
			}
		//Ū�����
			progressNum += 10;
			Refresh(WorkState,progressbar,progressNum);
			Huffmen InHuff = new Huffmen(CodeBook);
			byte[] data = InHuff.decoderConnect(In,height*width*3,progressbar,WorkState);
			progressNum = progressbar.getValue();
		//�}�l�ѽX
			System.out.println("�إ߶��j�}�C!");
			int[] ClassRange = new int [encodeClass+1];
			ClassRange[encodeClass] = 256;
			for(int i = 0;i < encodeClass;i++){
				ClassRange[i] = (255/encodeClass)*i;
			}
			short[][][] temp = new short [3][height][width];
			System.out.println("�}�l�ѽX!");
			int progressRange = (int)Math.round(height*width/15);
			for(int i=0;i<3;i++){
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						int j = data[i*height*width+y*width+x];
						temp[i][y][x] = (short)((ClassRange[j]+ClassRange[j+1])/2);
						if(ShotDown) return;
						if((i*height*width+y*height+x)%progressRange==0){
							progressNum++;
							Refresh(WorkState,progressbar,progressNum);
						}
					}
				}
			}
			System.out.println("�ѽX����!");
			System.out.println("�}�l�g��!");
			ImgTool.SetRGB(temp,OutFile);
			WorkState.setText("����i��:100%");
			WorkState.setForeground(Color.blue);
			progressbar.setValue(100);
    }
}
class OverPowZip extends MyThread{
	File InFile;
	File OutFile;
	JLabel WorkState;
	JProgressBar progressbar;
	JFrame demo;
	private int encodeClass;
	public OverPowZip(File InFile,File OutFile,JLabel WorkState,JProgressBar progressbar,JFrame demo,int encodeClass){
		this.InFile = InFile;
		this.OutFile = OutFile;
		this.WorkState = WorkState;
		this.progressbar = progressbar;
		this.demo = demo;
		this.encodeClass = encodeClass;
	}
	public void run() { 
		int schedule = 5;
		System.out.println("CL");
		Refresh(WorkState,progressbar,schedule);
        ImageE ImgTool = new ImageE();
		ImgTool.dataLength = encodeClass;
		ImgTool.SetOut(OutFile);
		System.out.println("�}�lŪ��:");
		System.out.println("Ū���ɦW:"+InFile.getPath());
		short[][][] data = ImgTool.GetRGB(InFile);
		int height = data[0].length;
		int width = data[0][0].length;
		System.out.println("X�b:"+width);
		System.out.println("Y�b:"+height);
		System.out.println("���Y����:"+encodeClass);
		System.out.println("Ū�ɦ��\!");
		System.out.println("�إߥ~�s�ɮ�!");
		System.out.println("�x�s�ɮ׳]�w!");
		ImgTool.WriteToFile((short)height);
		ImgTool.WriteToFile((short)width);
		ImgTool.WriteToFile((byte)encodeClass);
		System.out.println("�}�l����!");
		int xNum=0,yNum=0;
		xNum = (int)Math.ceil(width/(encodeClass-2));
		yNum = (int)Math.ceil(height/(encodeClass-2));
		int scheduleRange;
		if(xNum > 90) scheduleRange = (int)Math.round((double)xNum/90);
		else scheduleRange = 1;
		System.out.println("X�b���:"+xNum);
		System.out.println("Y�b���:"+yNum);
		for(int xCont=0;xCont<xNum;xCont++){
			for(int yCont=0;yCont<yNum;yCont++){
				if(ShotDown){
					ImgTool.CloseDataOut();
					ImgTool.FileDel(OutFile);
					return;
				}
				//���ιϤ���l��
				short[][][] temp = new short[3][encodeClass][encodeClass];//RGB,Y,X
				int ySite = yCont*(encodeClass-2)-1;//y�b���W���y��
				int xSite = xCont*(encodeClass-2)-1;//X�b���W���y��
				for(int i=0;i<3;i++){
					for(int y=0;y<encodeClass;y++){
						for(int x=0;x<encodeClass;x++){
							int yTemp = y+ySite;
							int xTemp = x+xSite;
							if((yTemp<height)&&(yTemp>=0)&&(xTemp<width)&&(xTemp>=0)) temp[i][y][x] = data[i][yTemp][xTemp];
							else if((y+ySite>=height)||(y+ySite<0)) temp[i][y][x] = 255;
							else if((x+xSite>=width)||(x+xSite<0)) temp[i][y][x] = 255;
						}
					}
					ImgTool.WriteToFile(ImgTool.OverPowON(temp[i]));
				}
			}
			if(((xCont+1)%scheduleRange)==0){
				schedule++;
				Refresh(WorkState,progressbar,schedule);
			}
		}
		ImgTool.CloseDataOut();
		WorkState.setText("����i��:100%");
		WorkState.setForeground(Color.blue);
		progressbar.setValue(100);
		JOptionPane.showMessageDialog(demo,"���Y����\n���Y��:"+((float)InFile.length()/(float)OutFile.length()),"�T��",JOptionPane.INFORMATION_MESSAGE); 
    }
}
class OverPowUnZip extends MyThread{
	File InFile;
	File OutFile;
	JLabel WorkState;
	JProgressBar progressbar;
	public OverPowUnZip(File InFile,File OutFile,JLabel WorkState,JProgressBar progressbar){
		this.InFile = InFile;
		this.OutFile = OutFile;
		this.WorkState = WorkState;
		this.progressbar = progressbar;
	}
	// override Thread's run()
	public void run() { 
		int schedule = 5;
		Refresh(WorkState,progressbar,schedule);
		ImageE ImgTool = new ImageE();
		ImgTool.SetIn(InFile);
		System.out.println("�}�lŪ��:");
		//Ū�X�e���INT 
		int height = ImgTool.ReadFromFileAsShort();
		int width = ImgTool.ReadFromFileAsShort();
		int encodeClass = ImgTool.ReadFromFileAsByte();
		ImgTool.dataLength = encodeClass;
		short[][][] Out = new short[3][height][width];
		System.out.println("X�b:"+width);
		System.out.println("Y�b:"+height);
		int xNum=0,yNum=0;
		xNum = (int)Math.ceil(width/(encodeClass-2));
		yNum = (int)Math.ceil(height/(encodeClass-2));
		int scheduleRange;
		if(xNum > 90) scheduleRange = (int)Math.round((double)xNum/90);
		else scheduleRange = 1;
		System.out.println("X�b���:"+xNum);
		System.out.println("Y�b���:"+yNum);
		for(int xCont=0;xCont<xNum;xCont++){
			for(int yCont=0;yCont<yNum;yCont++){
				if(ShotDown) return;
				short[][][] temp = new short[3][encodeClass][encodeClass];//RGB,Y,X
				int ySite = yCont*(encodeClass-2)-1;//y�b���W���y��
				int xSite = xCont*(encodeClass-2)-1;//X�b���W���y��
				//�h����ɰ϶��üg�J�ɮ�
				for(int i=0;i<3;i++){
					//Ū��dat�ä�����
					temp[i] = ImgTool.OverPowOFF(ImgTool.ReadFromFileAsByte());
					for(int y=0;y<encodeClass;y++){
						for(int x=0;x<encodeClass;x++){
							int yTemp = y+ySite;
							int xTemp = x+xSite;
							if((yTemp<height)&&(yTemp>=0)&&(xTemp<width)&&(xTemp>=0)) Out[i][yTemp][xTemp] = temp[i][y][x];
						}
					}
				}
			}
			if((xCont+1)%scheduleRange==0){
				schedule++;
				Refresh(WorkState,progressbar,schedule);
			}
		}
		ImgTool.SetRGB(Out,OutFile);
		WorkState.setText("����i��:100%");
		WorkState.setForeground(Color.blue);
		progressbar.setValue(100);
    }
}
/*
	Class ImagePreviewer �P Class FileIconView
	���ܩ�:http://www.cnblogs.com/wxcblog/archive/2012/11/06/2757015.html
*/
class ImagePreviewer extends JLabel {
 private static final long serialVersionUID = 1L;
//http://fly-dolphin.blogspot.tw/2011/08/static-final-long-serialversionuid.html
//���ӬO�ΨӰ��Y��pacjage�������ݮe

 public ImagePreviewer(JFileChooser chooser) {
  setPreferredSize(new Dimension(100, 100));
  //�]�w����ɮ׮ɪ��j�p
  
  setBorder(BorderFactory.createEtchedBorder());
  //�ڧ䤣�X�o�ӪF���F�¥�...
  
  chooser.addPropertyChangeListener(new PropertyChangeListener() {
   //�]�w��ť���� �����i�����ʧ@���s�g �p�G����ť���ܧ󪺸� �N����H�U�{��
   
   public void propertyChange(PropertyChangeEvent event) {
    if (event.getPropertyName() == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
		//�p�G�����o���ɮצW�ٸ�̷s���ܪ��ɮצW�٬ۦP
		//�ھ� https://www.javaworld.com.tw/jute/post/view?bid=5&id=179616 ��4��
		//�קK��J�ɮ׸������ɮצW�٩Ϊ̬����ݩʤ��P
		//by http://www.apihome.cn/api/java/JFileChooser.html
		
	File f = (File) event.getNewValue();
	//���o�̷s����J���|
	
     if (f == null) {
      setIcon(null);
      return;
     }
     ImageIcon icon = new ImageIcon(f.getPath());
	 //imageicon �������ݩ� http://www.apihome.cn/api/java/ImageIcon.html
	 //�Hf.getpath()�����|�h�إߤ@�ӹ���
	 
     if (icon.getIconWidth() > getWidth()) {
		 //�p�G���ɼe�פӼe �h���s�]�w
      icon = new ImageIcon(
	  
	  icon.getImage().getScaledInstance(getWidth(), -1, Image.SCALE_DEFAULT));
	  //�Ӽe�h���o�������e�סA�ó]�w���Ӽe��
	  // -1 �h��ܺ����������
	  //Image.SCALE_DEFAULT �h��ܸӹ��ɭ��s���˪���k����
	  //getScaledInstance(int width,int height,int hints) 
	  //����http://www.apihome.cn/api/java/Image.html
	  
      setIcon(icon);
     }
    }
   }
  });
 }
}
//�򥻳]�w
class FileIconView extends FileView {
 private FileNameExtensionFilter fliter;
 private Icon icon;

 public FileIconView(FileNameExtensionFilter filter, Icon anIcon) {
  fliter = filter;
  icon = anIcon;
 }
 public Icon getIcon(File f) {
  if (f.isDirectory() && fliter.accept(f)) {
   return icon;
  } else {
   return null;
  }
 }

 public void setIcon(Icon icon) {
  this.icon = icon;
 }
}
class GuiBulider implements ActionListener{
	File InFile = null;
	File OutFile = null;
	JFrame demo;
	JLabel InState;//��J�ɮת��A
	JLabel OutState;//��X���|���A
	private final Font LabelFont = new Font("Monospaced",Font.BOLD,15);
	JLabel WorkState = new JLabel("����i��:");
	JButton button;
	JButton path;
	JButton open;
	JFileChooser fileChooser;
	JComboBox box1;
	JComboBox box2;
	JComboBox box3;
	JProgressBar progressbar;
	MyThread t1;
	Timer timer;
	JLabel ImgWatch;
	private boolean InReady = false;
	private boolean OutReady = false;
	private boolean Ziptype = true;
	private boolean Worktype = true;
	private float OrgainSize = 0;
	private int encodeClass=3;
	
	public GuiBulider(URL background,URL Icon){
		demo = new JFrame("���Y&�����Y");
		demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        demo.setLayout(new GridLayout(1,2));
		JPanel Left = new JPanel(new GridLayout(4,1));//��\���
		ImageIcon  backgroundpic = new ImageIcon(background);
		demo.setIconImage(new ImageIcon(Icon).getImage());
			//�u�@�Ҧ�
				JLabel label1 = new JLabel("�Ҧ�");//����
				label1.setFont(LabelFont);
				String[] type={"���Y","�����Y"};//�u�@�Ҧ��}�C
				box1 = new JComboBox(type);//�s�W�U�Ԧ����
				box1.addActionListener (this);
		
			//���Y�覡 �w�]CL
				JLabel label3 = new JLabel("���Y�覡");//����
				label3.setFont(LabelFont);
				String[] type3={"VQ","OverPow"};//�u�@�Ҧ��}�C
				box3 = new JComboBox(type3);//�s�W�U�Ԧ����
				box3.addActionListener (this);
			
			//���Y���
				JLabel label2 = new JLabel("���Y����");//����
				label2.setFont(LabelFont);
				String[] type2={"3","4","5","6","7","8","9"};//���YCLASS�}�C
				box2 = new JComboBox(type2);//�s�W�U�Ԧ����
				box2.addActionListener (this);//�U�Ԧ����listener
			
			//���s��
				open=new JButton("��J�ɮ�");
				open.addActionListener(this);
				
				path = new JButton("��X�ɮ�");
				path.addActionListener(this);
				
				button = new JButton("�}�l����");
				button.setEnabled(false);
				button.addActionListener(this);
				
			//�u�@���A
				JLabel StateLabel = new JLabel("�u�@���p:");
				StateLabel.setFont(LabelFont);
				InState = new JLabel("����ܿ�J�ɮ�!");
				InState.setFont(LabelFont);
				OutState = new JLabel("����ܿ�X�ɮ�!");
				OutState.setFont(LabelFont);	
				
			//�i�ױ�
				WorkState.setFont(LabelFont);
				progressbar = new JProgressBar();
				progressbar.setOrientation(JProgressBar.HORIZONTAL);
				progressbar.setMinimum(0);
				progressbar.setMaximum(100);
				progressbar.setValue(0);
				progressbar.setStringPainted(true);
				progressbar.setPreferredSize(new Dimension(300,20));
				progressbar.setBorderPainted(true);
				progressbar.setBackground(Color.pink);
				
				timer=new Timer(100,this); 
				
			
			//�Ϥ��w��
				ImgWatch = new JLabel("�|���[���Ϥ�!");
				ImgWatch.setFont(LabelFont);	
			//�[�J�ƪ�
				JLabel imgLabel = new JLabel(backgroundpic);//?�I��?��b??���C
				demo.getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));//�`�N?���O??�A?�I��??�K�[��jfram��LayeredPane���O���C
				imgLabel.setBounds(0,0,backgroundpic.getIconWidth(), backgroundpic.getIconHeight());//?�m�I��??����m  		
				Container cp=demo.getContentPane();  
				cp.setLayout(null);
				open.setBounds(80, 420, 150, 40);	
				path.setBounds(80, 470, 150, 40);	
				button.setBounds(80, 520, 150, 40);
				label1.setBounds(300, 110, 100, 50);	
				label3.setBounds(400, 110, 100, 50);	
				label2.setBounds(500, 110, 100, 50);
				box1.setBounds(300, 150, 100, 50);
				box3.setBounds(400, 150, 100, 50);
				box2.setBounds(500, 150, 100, 50);
				StateLabel.setBounds(300, 200, 100, 50);
				InState.setBounds(300, 300, 200, 50);
				OutState.setBounds(300, 400, 200, 50);
				WorkState.setBounds(300, 480, 300, 50);
				progressbar.setBounds(300, 535, 862, 50);
				ImgWatch.setBounds(700, 50, 450, 420);
				cp.add(open);
				cp.add(path);
				cp.add(button);
				cp.add(label1);
				cp.add(label3);
				cp.add(label2);
				cp.add(box1);
				cp.add(box2);
				cp.add(box3);
				cp.add(StateLabel);
				cp.add(InState);
				cp.add(OutState);
				cp.add(WorkState);
				cp.add(progressbar);
				cp.add(ImgWatch);
				((JPanel)cp).setOpaque(false); //�`�N?���A??�e���O??�z���C??LayeredPane���O�����I���~��?�ܥX?�C
			
			demo.pack();
			demo.setVisible(true);
			demo.setSize(1200,729);
			demo.setResizable(false);//���^�j�p���i��
	}
	public void ImgFresh(boolean Worktype){
		File file;
		if(Worktype) file = InFile;
		else file = OutFile;
		int imgWidth = 0;
		int imgHeight = 0;
		try{
			BufferedImage BI = ImageIO.read(file);
			imgWidth = BI.getWidth();  
			imgHeight = BI.getHeight();
		}catch(IOException e){
			System.out.println("error!");
			return;
		}
		Image image = demo.getToolkit().getImage(file.getPath());
		if((imgWidth > 380 ) || (imgHeight >380)){							
			int conWidth  = 380;
			int conHeight = 380;
			int reImgWidth;
			int reImgHeight;  
			if(imgWidth/imgHeight>=conWidth/conHeight){  
				if(imgWidth>conWidth){  
					reImgWidth=conWidth;  
					reImgHeight=imgHeight*reImgWidth/imgWidth;  
				}else{  
					reImgWidth=imgWidth;  
					reImgHeight=imgHeight;  
				}  
			}else{
				if(imgWidth>conWidth){  
					reImgHeight=conHeight;  
					reImgWidth=imgWidth*reImgHeight/imgHeight;  
				}else{  
					reImgWidth=imgWidth;  
					reImgHeight=imgHeight;  
				}  
			}
			
			image = image.getScaledInstance(reImgWidth, reImgHeight, Image.SCALE_DEFAULT);
		}
		ImgWatch.setIcon(new ImageIcon(image));
		ImgWatch.setText("");
	}
	public void actionPerformed(ActionEvent e){
		int result;
		if(e.getSource()==box1){
			if(box1.getSelectedItem().toString().equals("���Y")){
				Worktype = true;
				box2.setEnabled(true);
			}else{
				Worktype = false;	
				box2.setEnabled(false);
			} 
			button.setEnabled(false);
			open.setEnabled(true);
			path.setEnabled(true);
			InState.setText("����ܿ�J�ɮ�!");
			OutState.setText("����ܿ�X�ɮ�!");
			ImgWatch.setIcon(null);
			ImgWatch.setText("�|���[���Ϥ�");
			InReady = false;
			OutReady = false;
		}
        if(e.getSource()==box2){
			encodeClass = Integer.parseInt(box2.getSelectedItem().toString());
		}
		if(e.getSource()==box3){
			if(box3.getSelectedItem().toString().equals("CL")){
				Ziptype = true;
			}else{
				Ziptype = false;
			}
			if(Worktype) box2.setEnabled(true);
			else box2.setEnabled(false);
			button.setEnabled(false);
			open.setEnabled(true);
			path.setEnabled(true);
			InState.setText("����ܿ�J�ɮ�!");
			OutState.setText("����ܿ�X�ɮ�!");
			ImgWatch.setIcon(null);
			ImgWatch.setText("�|���[���Ϥ�");
			InReady = false;
			OutReady = false;
		}
		/*��Τ���U"���}���"���s��,JFileChooser��showOpenDialog()��k�|��X����ܮ�,�çQ��setApproveButtonText()��k���N���s�W"Open"��r;�HsetDialogTitle()��k�]�m���}����ܮ�Title�W��.��ϥο�ܧ���,�|�N��ܵ��G�s��result�ܶq��.
         */
        if (e.getSource()==open){
			fileChooser = new JFileChooser(".");
			if(Worktype){
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "gif", "bmp");
				fileChooser.setFileFilter(filter);
				fileChooser.setAccessory(new ImagePreviewer(fileChooser));
				// ImagePreviewer �w���Ϥ� �åB�x�s�Ϥ����ɮ׸��| �������147��
				fileChooser.setFileView(new FileIconView(filter,new ImageIcon()));
				//FileIconView �̫������ܪ�����
			}else{
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Data file", "dat");
				fileChooser.setFileFilter(filter);
			}
			fileChooser.setApproveButtonText("�T�w");
            fileChooser.setDialogTitle("����ɮ�");
            result = fileChooser.showOpenDialog(demo);

            /*��Τ���U���}����ܮت�"�T�w"�s��,�ڭ̴N�i�H�Q��
				getSelectedFile()��k���o����H.�Y�O�Τ���U���}����ܮت�"Cancel"�s,�h�N�blabel�W���"�A�S����ܥ�����"�r��.
             */
            if (result == JFileChooser.APPROVE_OPTION){
                InFile = fileChooser.getSelectedFile();
                InState.setText("��J�ɮפw�N��!");
				InReady = true;
				ReadyCheck();
				if(InFile.length()>0) if(Worktype) ImgFresh(Worktype);
            }
        }
		if (e.getSource()==path){
			fileChooser = new JFileChooser(".");
			if(Worktype){
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Data file", "dat");
				fileChooser.setFileFilter(filter);
			}else{
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "gif", "bmp");
				fileChooser.setFileFilter(filter);
			}
			fileChooser.setDialogTitle("�]�w��X�ɮ�");
			int intRetVal = fileChooser.showSaveDialog(demo); 
			if( intRetVal == JFileChooser.APPROVE_OPTION){
				OutState.setText("��X�ɮפw�]�w!");
				OutReady = true;
				String FilePath = fileChooser.getSelectedFile().getPath();
				int endIndex = FilePath.lastIndexOf(46);
				String Type = FilePath.substring(endIndex+1);
				if(Worktype){
					if(!Type.equals("dat")||endIndex==0){
						FilePath += ".dat";
						OutFile = new File(FilePath);
					}else{
						OutFile = fileChooser.getSelectedFile();
					}
				}else{
					if(Type.equals("")||endIndex==-1){
						FilePath += ".jpg";
						OutFile = new File(FilePath);
					}else{
						OutFile = fileChooser.getSelectedFile();
					}
				}
				ReadyCheck();
			}
		}
		if (e.getSource()==button){
			if(button.getText().equals("�}�l����")){
				imageZip(); 
				timer.start();
				button.setText("�פ����");
				path.setEnabled(false);
				open.setEnabled(false);
				box1.setEnabled(false);
				box2.setEnabled(false);
				box3.setEnabled(false);
			}else{
				WorkState.setText("����i��:�w�g�פ����!");
				WorkState.setForeground(Color.red);
				progressbar.setValue(0);
				t1.callShowDown();
				button.setEnabled(false);
				open.setEnabled(true);
				path.setEnabled(true);
				button.setText("�}�l����");
				InState.setText("����ܿ�J�ɮ�!");
				OutState.setText("����ܿ�X�ɮ�!");
				ImgWatch.setIcon(null);
				ImgWatch.setText("�|���[���Ϥ�");
				timer.stop();
				InReady = false;
				OutReady = false;
				box1.setEnabled(true);
				if(Worktype) box2.setEnabled(true);
				else box2.setEnabled(false);
				box3.setEnabled(true);
			}
		}
		if(e.getSource()==timer){
			int value=progressbar.getValue();
			if(value>=100){
				WorkState.setText("����i��:�w�g��������!");
				WorkState.setForeground(Color.red);
				progressbar.setValue(0);
				button.setEnabled(false);
				open.setEnabled(true);
				path.setEnabled(true);
				button.setText("�}�l����");
				InState.setText("����ܿ�J�ɮ�!");
				OutState.setText("����ܿ�X�ɮ�!");
				timer.stop();
				InReady = false;
				OutReady = false;
				box1.setEnabled(true);
				if(Worktype){
					box2.setEnabled(true);
					ImgWatch.setIcon(null);
					ImgWatch.setText("�|���[���Ϥ�");
				}else{
					box2.setEnabled(false);
					ImgFresh(Worktype);
				} 
				box3.setEnabled(true);
			}
		}
    }
	private void ReadyCheck(){
		if(InReady&&OutReady) button.setEnabled(true);
	}
	private void imageZip(){
		if(Worktype){//�������Y
			if(Ziptype) t1 = new CLZip(InFile,OutFile,WorkState,progressbar,demo,encodeClass); // ����Thread����
			else t1 = new OverPowZip(InFile,OutFile,WorkState,progressbar,demo,encodeClass); // ����Thread����
			t1.start(); // �}�l����t1.run()
		}else{//��������Y
			if(Ziptype) t1 = new CLUnZip(InFile,OutFile,WorkState,progressbar); // ����Thread����
			else t1 = new OverPowUnZip(InFile,OutFile,WorkState,progressbar); // ����Thread����
			t1.start(); // �}�l����t1.run()
		}
	}
}
class HuffmenNode{
	private String Code;
	private short Value;
	private int Frq = 0;//�X�{�W�v
	public HuffmenNode(short Value){
		this.Value = Value;
	}
	public void setCode(String Code){
		this.Code = Code;
	}
	public void setFrq(int Frq){
		this.Frq = Frq;
	}
	public void addFrq(){
		Frq++;
	}
	public String getCode(){
		return Code;
	}
	public short getValue(){
		return Value;
	}
	public int getFrq(){
		return Frq;
	}
}
class Huffmen{
	private HuffmenNode[] Node;
	private int Longest=1;
	public Huffmen(HuffmenNode[] Node){
		this.Node = Node;
	}
	public Huffmen(){
		
	}
	public int[] EncoderConnect(byte[] Data,JProgressBar progressbar,JLabel WorkState){
		System.out.println("�}�l�Ƨ�!");
		Sort(false);
		System.out.println("�}�l�s�X!");
		Encoder();
		System.out.println("�}�l�Ƨ�!");
		Sort(true);
		System.out.println("�}�l�s�X!");
		int progressNum = progressbar.getValue();
		int progressRange = (int)Math.round(Data.length/31);
		int[] temp = new int[Data.length/(31/Longest)];
		System.out.println(Data.length/(31/Longest));
		int ReadyEncode = 0;
		String OverData = "";
		String TempData = "";
		int tempLength=0;
		while(true){
			while(TempData.length()<31){
				if(ReadyEncode%progressRange==0){
					progressNum ++;
					MyThread.Refresh(WorkState,progressbar,progressNum);
				}
				if(ReadyEncode>=Data.length){
					System.out.println("UsedArea:"+tempLength);
					int[] Out = new int[tempLength+1];
					for(int i=0;i<=tempLength;i++){
						Out[i] = temp[i];
					}
					return Out;
				} 
				String CodeTemp = Node[Data[ReadyEncode]].getCode();
				if((TempData.length() + CodeTemp.length())<32){
					TempData += CodeTemp;
					ReadyEncode++;
				}else{
					int WriteLength = 31 - TempData.length();
					String WriteData = CodeTemp.substring(0,WriteLength);
					OverData = CodeTemp.substring(WriteLength);
					TempData += WriteData;
				}
			}
			temp[tempLength++] = Integer.parseInt(TempData,2);
			if(!OverData.equals("")){
				TempData = OverData;
				OverData = "";
				ReadyEncode++;
			}else TempData = "";
		}
	}
	public byte[] decoderConnect(int[] Data,int LENGTH,JProgressBar progressbar,JLabel WorkState){
		byte[] Out = new byte[LENGTH];
		int progressNum = progressbar.getValue();
		int progressRange = (int)Math.round(LENGTH/31);
		String CodeData = "";
		System.out.println("�ѽX");
		int ReadyDecode = 0;
		for(int i=0;i<Data.length;i++){
			int dataLength = 31;
			while(dataLength>0){ 
				int bitOfdata = (Data[i]>>(--dataLength)) & 1;
				CodeData += Integer.toString(bitOfdata);
				if(bitOfdata==0){
					if(ReadyDecode%progressRange==0){
						progressNum ++;
						MyThread.Refresh(WorkState,progressbar,progressNum);
					}
					if(ReadyDecode==LENGTH) return Out;
					Out[ReadyDecode++] = Decoder(CodeData);
					CodeData = "";
				}
			}
		}
		return Out;
	}
	private byte Decoder(String Data){
		byte Out;
		for(int i=0;i<Node.length;i++){
			if(Data.equals(Node[i].getCode())){
				Out = (byte)Node[i].getValue();
				return Out;
			}
		}
		return -1;
	}
	private void Encoder(){
		for(int i=(Node.length-1);i>=0;i--){
			int Long = 1;
			if(Node[i].getFrq()>0){
				String temp = "";
				for(int j=0;j<(Node.length-i-1);j++){
					temp += "1";
					Long++;
				}
				temp += "0";
				Node[i].setCode(temp);
			}
			if(Long>Longest) Longest = Long;
		}
	}
	private void Sort(boolean type){
        Sort(0, Node.length - 1,type);
    }
    private void Sort(int left, int right,boolean type){
        if (right <= left)
            return;
        int pivotIndex = (left + right) / 2;
        int pivot;
		if(type) pivot = Node[pivotIndex].getValue();
		else pivot = Node[pivotIndex].getFrq();
        Swap(pivotIndex, right);
        int swapIndex = left;
        for (int i = left; i < right; ++i){
            if (Node[i].getValue() <= pivot){
                Swap(i, swapIndex);
                ++swapIndex;
            }
        }
        Swap(swapIndex, right);
        Sort(left, swapIndex - 1,type);
        Sort(swapIndex + 1, right,type);
    }
    private void Swap(int indexA, int indexB){
        HuffmenNode tmp = Node[indexA];
        Node[indexA] = Node[indexB];
        Node[indexB] = tmp;
    }
}
public class Demo{
	public static void main(String[] args) {
		URL background = Demo.class.getResource("B.jpg");
		URL Icon = Demo.class.getResource("icon.png");
		new GuiBulider(background,Icon);
    }
	
}