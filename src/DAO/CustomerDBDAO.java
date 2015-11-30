package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import Beans.Coupon;
import Beans.CouponType;
import Beans.Customer;
import Connection.ConnectionPool;
import Connection.CouponSystemException;
import Utility_classes.dbMethods;

public class CustomerDBDAO implements CustomerDAO {
	
	private final ConnectionPool cp;
	
	public CustomerDBDAO(ConnectionPool cp) {
		this.cp = cp;
		cp = ConnectionPool.getInstance();
	}

	@Override
	public long create(Customer c) throws CouponSystemException {
		
		long result;
		Connection con = null;
		try{
			ConnectionPool cp = ConnectionPool.getInstance();
			con = cp.getConnection();
			String query = "INSERT into Customer(cust_name, password values(?,?)";
			PreparedStatement pstmt = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, c.getCustName());
			pstmt.setString(2, c.getPassword());
			pstmt.executeQuery();
			result = dbMethods.getIntFromResultset(pstmt);
			cp.returnConnection(con);
		}catch (SQLException e) {
			throw new CouponSystemException("Could not create customer in DB");			
		}finally {
			try {
				DBDAO.returnConnectionToPool(con);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
	}

	@Override
	public void remove(Customer c) throws CouponSystemException {
		
		Connection con = null;
		try{
			ConnectionPool cp = ConnectionPool.getInstance();
			con = cp.getConnection();
			String query = "DELETE from Customer where id=?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setLong(1, c.getId());
			pstmt.executeUpdate();
			cp.returnConnection(con);
		}catch (SQLException e) {
			throw new CouponSystemException("Could not delete customer from DB");			
		}finally {
			try {
				DBDAO.returnConnectionToPool(con);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void update(Customer c) throws CouponSystemException {
		
		Connection con = null;
		
		try {
			ConnectionPool cp = ConnectionPool.getInstance();
			con = cp.getConnection();
			String query = "UPDATE customer set cust_name=?,password=? where id=?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, c.getCustName());
			pstmt.setString(2, c.getPassword());
			pstmt.setLong(3, c.getId());
			pstmt.executeUpdate();
			cp.returnConnection(con);
		}catch (SQLException e) {
			throw new CouponSystemException("Could not update customer in DB");			
		}finally {
			try {
				DBDAO.returnConnectionToPool(con);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public Customer get(long id) throws CouponSystemException {
		
		Connection con = null;
		Customer result = new Customer(id, "", "");
		try {
			ConnectionPool cp = ConnectionPool.getInstance();
			con = cp.getConnection();
			String query = "Select cust_name, password from Customer where id=?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				result.setCustName(rs.getString("cust_name"));
				result.setPassword(rs.getString("password"));
			}
			cp.returnConnection(con);
		}catch (SQLException e) {
			throw new CouponSystemException("Could not read customer from DB");			
		}finally {
			try {
				DBDAO.returnConnectionToPool(con);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return result;
	}

	@Override
	public Collection<Customer> getAll() throws CouponSystemException {
		
		Connection con = null;
		Collection<Customer> result = new ArrayList<>();
		try {
			ConnectionPool cp = ConnectionPool.getInstance();
			con = cp.getConnection();
			String query = "select id ,cust_name, password from customer";
			PreparedStatement pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result.add(new Customer(rs.getLong("id"), rs.getString("cust_name"), rs.getString("password")));
			}
			cp.returnConnection(con);
		}catch (SQLException e) {
			throw new CouponSystemException("Could not get all customers from DB");			
		}finally {
			try {
				DBDAO.returnConnectionToPool(con);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
	}

	@Override
	public Collection<Coupon> getCoupons(Customer c) throws CouponSystemException {
		
		Connection con = null;
		Collection<Coupon> result = new ArrayList<>();
		try {
			ConnectionPool cp = ConnectionPool.getInstance();
			con = cp.getConnection();
			String query = "SELECT id,title,start_date,end_date,amount,type,message,price,image from Coupon cp, Customer_Coupon cc where cp.id=cc.coupon_id and cc.cust_id=?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setLong(1, c.getId());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result.add(new Coupon(rs.getLong("id"), rs.getString("title"), rs.getDate("start_date"), rs.getDate("end_date"), rs.getInt("amount"), CouponType.valueOf(rs.getString("type")), rs.getString("message"), rs.getDouble("price"), rs.getString("image")));
			}
			cp.returnConnection(con);
		}catch (SQLException e) {
			throw new CouponSystemException("Could not get all customer's coupons from DB");			
		}finally {
			try {
				DBDAO.returnConnectionToPool(con);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return result;
	}

	@Override
	public boolean IsValidPassword(String custName, String password) throws CouponSystemException {
		// TODO Auto-generated method stub
		return false;
	}

}
