package com.springportfolio.dao.boards;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Repository;

import com.springportfolio.domain.boards.Board;

@Repository("boardDao")
public class MyBatisBoardDAO implements BoardDAO {
	private static final Logger log = LoggerFactory.getLogger(MyBatisBoardDAO.class);
	@Autowired
	private SqlSession sqlSession;
	@Autowired
	private DataSource dataSource;
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@PostConstruct
	public void initialize(){
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("import.sql"));
		DatabasePopulatorUtils.execute(populator,dataSource);
		log.info("database initialized success!");
	}

	@Override
	public List<Board> select(String skey, String sval, int start, int end) {
		Board board = new Board();
		board.setSkey(skey);
		board.setSval(sval);
		board.setStart(start);
		board.setEnd(end);
		return sqlSession.selectList("BoardMapper.select", board);
	}

	@Override
	public void create(Board board) {
		sqlSession.insert("BoardMapper.createBoard", board);
	}

	@Override
	public Board selectOne(int num) {
		return sqlSession.selectOne("BoardMapper.selectOne", num);
	}

	@Override
	public void updateBoard(Board updateBoard) {
		sqlSession.update("BoardMapper.updateBoard", updateBoard);
	}

	@Override
	public void delete(int i) {
		sqlSession.delete("BoardMapper.deleteBoard", i);
	}

	@Override
	public void updateCount(int i, int j) {
		sqlSession.update("BoardMapper.updateCount", new Board(i, j));
	}
	
	@Override
	public int selectCount(String skey, String sval) {
		Board board = new Board();
		board.setSkey(skey);
		board.setSval(sval);
		return sqlSession.selectOne("BoardMapper.selectCount", board);
	}
	
}
