#自定义View系统学习笔记-第四期：Canvas的辅助绘制
##  1 ，范围裁切
### 1.1 clipRect(int left,int top,int right,int bottom)  /  clipRect(RectF rectf,Region.Op op)  /clipRect(Rect rect,Region.Op op) 
	参数：
		Rect 位置坐标
		RectF 位置坐标
		Region.Op 
			DIFFERENCE  A图中不同于B的部分显示出来
			REPLACE  只显示B的形状
			REVERSE_DIFFERCENCE  B形状中不同于A的部分显示出来，默认的
			INTERSECT  A和B的交集形状
			UNION  A和B的全集形状
			XOR   全集形状减去交集形状后的部分
![](http://img.blog.csdn.net/20161218202418687)
![](http://img.blog.csdn.net/20161218201408423)
![](http://img.blog.csdn.net/20161218201502071)
![](http://img.blog.csdn.net/20161218201558019)
![](http://img.blog.csdn.net/20161218201643259)
![](http://img.blog.csdn.net/20161218201719166)

### 1.2 clipPath(Path path,Region.OP op) 
	比clipRect多了很多种形状。
	
## 2 ，几何变换
	几何变换的使用大概分为3类：
	1，使用Canvas来做常见的二维变换；
	2，使用Matrix来做常见和不常见的二维变换；
	3，使用Camera来做三维变换。
### 2.1 使用Canvas来做常见的二维变换：
	使用canvas来组合变换的时候，要注意顺序。
#### 2.1.1 canvas.translate(float dx,float dy) 平移
	参数：
		dx 横向位移
		dy 纵向位移
![](https://user-gold-cdn.xitu.io/2017/8/4/ce71396d00dba6af67c6b9a6be5a80e6?imageView2/0/w/1280/h/960/ignore-error/1)
#### 2.1.2 canvas.rotate(float degress,float px,float py) 旋转
	参数：
	degress 旋转的角度
	px，py 轴心的坐标	
![](https://user-gold-cdn.xitu.io/2017/8/4/a4f510606baacfc1ae91aedb8df7250f?imageView2/0/w/1280/h/960/ignore-error/1)	
#### 2.1.3 canvas.scale(float sx,float sy,float px,float py)收缩

	参数：
	sx sy 横向和纵向的放缩倍数
	px py  放缩的轴心
![](https://user-gold-cdn.xitu.io/2017/8/4/4e2f563019afbf7f1fdf54f10045fc8a?imageView2/0/w/1280/h/960/ignore-error/1)

#### 2.1.4 skew(float sx,float sy) 错切
	参数:
	sx sy：错切系数
![](https://user-gold-cdn.xitu.io/2017/8/4/33cb4f6082f7067c11a0bc956d018960?imageView2/0/w/1280/h/960/ignore-error/1) 

### 2.2 使用Matrix来变换
	Matrix比Canvas更灵活，preXXX()和postXXX()两种方法，preXXX()是在执行前，postXXX()是在执行后。每个变换的方法都有这两种不一样的方式。
	注意！！！：
		不同手机系统中setMatrix(Matrix)的行为可能不一致，所以还是用concat()来应用到canvas。
#### 2.2.1 使用Matrix来做常见变换
	Matrix做常见变换的方式：
		1，创建Matrix对象
		2，调用Matrix的pre/postTranslae/Rotate/Scale/skew方法来设置几何变换
		3，使用Canvas的setMatrix(Matrix)或Canvas.concat(Matrix)来应用到Canvas。
	
	有两个方法把matrix应用到canvas中：
		1，canvas.setMatrix(matrix) 看注意！！
		2，canvas.concat(matrix)
#### 2.2.2 使用Matrix来做自定义变换
	Matrix的自定义变换使用的是setPolyToPoly()方法。
##### 2.2.2.1 Matrix.setPolyToPoly(float[] src,int srcIndex,float[] dst,int dstIndex,int pointCount) 用点对点映射的方式设置变换
	Poly就是[多]的意思。setPolyToPoly()的作用是通过多点的映射的方式来直接设置变换。
	[多点映射]的意思就是把指定的点移动到给出的位置，从而发生改变。
	例如：(0,0)=>(100,100)表示把(0,0)位置的像素移动到(100,100)的位置，这个是单点的映射，单点银蛇可以实现平移，而多点的映射就可以绘制内容任意的扭曲。



例如：
	
	Matrix matrix = new Matrix();
	float pointsSrc = {left, top, right, top, left, bottom, right, bottom};
	float pointsDst = {left - 10, top + 50, right + 120, top - 90, left + 20, bottom + 30, right + 20, bottom + 60};
	
	...
	
	matrix.reset();
	matrix.setPolyToPoly(pointsSrc, 0, pointsDst, 0, 4);
	
	canvas.save();
	canvas.concat(matrix);
	canvas.drawBitmap(bitmap, x, y, paint);
	canvas.restore();

![](https://user-gold-cdn.xitu.io/2017/8/4/bec6994542e74ba6b5990887ae43832f?imageView2/0/w/1280/h/960/ignore-error/1)
