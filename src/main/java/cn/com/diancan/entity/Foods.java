package cn.com.diancan.entity;

public class Foods {
   /**
    * fid
    */
   private Integer fid;
   /**
    * 类型 1-大菜 2-荤菜 3-素菜 4-汤
    */
   private Integer type;
   /**
    * 是否显示 1-可点 2-不可点
    */
   private Integer show;
   /**
    * 菜名
    */
   private String name;
   /**
    * 价格
    */
   private Double price;

   public Integer getFid() {
      return fid;
   }

   public void setFid(Integer fid) {
      this.fid = fid;
   }

   public Integer getType() {
      return type;
   }

   public void setType(Integer type) {
      this.type = type;
   }

   public Integer getShow() {
      return show;
   }

   public void setShow(Integer show) {
      this.show = show;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Double getPrice() {
      return price;
   }

   public void setPrice(Double price) {
      this.price = price;
   }

   @Override
   public String toString() {
      return "Foods{" +
              "fid=" + fid +
              ", type=" + type +
              ", show=" + show +
              ", name='" + name + '\'' +
              ", price=" + price +
              '}';
   }
}
