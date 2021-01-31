package com.rockpaperscissors.dao.game;

import com.rockpaperscissors.dao.IDao;
import com.rockpaperscissors.model.game.Game;

/**
 * Specific abstraction for the games DAO layer.
 */
public interface IGamesDAO extends IDao<String, Game>
{
}
