package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Post;
import com.techelevator.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
<<<<<<< HEAD
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.Principal;
=======
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
>>>>>>> 10eee9847abd85a99ffab8938981a32e5c4647fd

@Component
public class JdbcPostDao implements PostDao {


    private final JdbcTemplate jdbcTemplate;

    public JdbcPostDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
<<<<<<< HEAD
=======
    public List<Post> getAllPosts() throws DaoException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts";

        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql);

            while (result.next()) {
                posts.add(mapRowToPost(result));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return posts;
    }

    @Override
>>>>>>> 10eee9847abd85a99ffab8938981a32e5c4647fd
    public Post createPost(Post post,int userId) {

        Post newPost = null;

        String sql = "INSERT INTO posts (caption, post_creator) values (?, ?) RETURNING post_id";

        try {
            int postId = jdbcTemplate.queryForObject(sql, int.class, post.getCaption(), post.getPostCreator());
            newPost = getPostById(postId);

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return newPost;
    }

    @Override
    public Post getPostById(int id) {

        Post post = null;
        String sql = "SELECT * FROM posts WHERE post_id = ?;";

        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);

            if (result.next()) {
                post = mapRowToPost(result);
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);

        }

<<<<<<< HEAD
        return post;
    }

    @Override
    public void deletePostById(int id) {

=======
        // Compute likes.
        post.setTotalLikes(countPostLikes(id));
        return post;
    }

    private int countPostLikes(int postId) {
        String sql = "SELECT COUNT(*) AS likes FROM likes WHERE post_id = ?;";

        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, postId);

            if (result.next()) {
                return result.getInt("likes");
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);

        }

        return 0;
    }

    @Override
    public boolean hasUserLikedPostById(int postId, String authorName) {
        String sql = "SELECT COUNT(*) AS likes FROM likes WHERE post_id = ? AND author_name = ?;";

        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, postId, authorName);
            if (result.next()) {
                return result.getInt("likes") > 0;
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);

        }

        return false;
    }

    @Override
    public int deletePostById(int id) {
        int numberOfRows = 0;
        String sql = "DELETE FROM posts WHERE post_id = ?";
        try{
            numberOfRows = jdbcTemplate.update(sql, id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return numberOfRows;
>>>>>>> 10eee9847abd85a99ffab8938981a32e5c4647fd
    }

    @Override
    public Post updatePost(Post post) {
<<<<<<< HEAD
        return null;
=======
        Post newPost = null;

        String sql = "UPDATE posts SET caption = ? WHERE post_id = ?";

        try {
            int numberOfRows = 0;
            numberOfRows = jdbcTemplate.update(sql, post.getCaption(), post.getPost_id());

            if(numberOfRows == 0){
                throw new DaoException("No number of rows affected");
            }
            else{
                newPost = getPostById(post.getPost_id());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return newPost;
    }

    @Override
    public void addLike(Post post, User author) {
        String sql = "INSERT INTO likes (post_id, author_name) values (?, ?)";

        try {
            int postId = jdbcTemplate.update(sql, post.getPost_id(), author.getUsername());
            if (0 == postId) {
                throw new DaoException("No like added");
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void removeLike(Post post, User author) {
        String sql = "DELETE FROM likes WHERE post_id = ? AND author_name = ?";

        try {
            int postId = jdbcTemplate.update(sql, post.getPost_id(), author.getUsername());
            if (0 == postId) {
                throw new DaoException("No like removed");
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
>>>>>>> 10eee9847abd85a99ffab8938981a32e5c4647fd
    }


    public Post mapRowToPost(SqlRowSet result){
        Post post = new Post();

        post.setPost_id(result.getInt("post_id"));
        post.setCaption(result.getString("caption"));
        post.setPostCreator(result.getString("post_creator"));

        return post;
    }

}
