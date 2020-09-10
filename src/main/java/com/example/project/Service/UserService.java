package com.example.project.Service;

import com.example.project.domain.User;

/**
 * @Title: project
 * @Package * @Description:     * @author CodingSir
 * @date 2020/9/714:37
 */
public interface UserService {
    User getById(int id);

    boolean tx();
}
