/**
* Daniel Ricci <thedanny09@gmail.com>
*
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without restriction,
* including without limitation the rights to use, copy, modify, merge,
* publish, distribute, sublicense, and/or sell copies of the Software,
* and to permit persons to whom the Software is furnished to do so, subject
* to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
* THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
* IN THE SOFTWARE.
*/

package game.player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import engine.core.mvc.model.BaseModel;
import game.ChessEntity;
import game.PawnEntity;

/**
 * Player class for the game 
 * 
 * @author Daniel Ricci <thedanny09@gmail.com>
 *
 */
public class Player extends BaseModel {

	/**
	 * The enumeration of teams available to all players 
	 * 
	 * @author Daniel Ricci <thedanny09@gmail.com>
	 *
	 */
	public enum PlayerTeam { WHITE, BLACK }
	
	/**
	 * The team that this player is associated
	 */
	private final PlayerTeam _team;
	
	/**
	 * The data values associated to the player
	 */
	private final Enum[] _dataValues;
	
	/**
	 * The list of entities owned by the player
	 */
	private final List<ChessEntity> _entities = new ArrayList();
	
	/**
	 * Constructs a new instance of this class type
	 * 
	 * @param team The team of the player
	 * @param dataValues The data values associated to the player
	 */
	public Player(PlayerTeam team, Enum[] dataValues) {
		_team = team;
		_dataValues = dataValues;
	}
	
	/**
	 * Loads the chess piece items that the player owns
	 */
	public void loadItems() {
		for(int i = 0; i < 16; ++i) {
			ChessEntity entity = new PawnEntity();
			entity.LinkData(this);
			_entities.add(entity);
		}
	}
	
	/**
	 * Gets the list of entities associated to the specified type
	 * 
	 * @param entityClass The entity type
	 * 
	 * @return A list of entities
	 */
	public <T extends ChessEntity> List<T> getEntities(Class<T> entityClass) {
		return (List<T>) _entities.stream().filter(z -> z.getClass() == entityClass).collect(Collectors.toList());
	}
	
	/**
	 * Gets the data values associated to the player 
	 * 
	 * @return The data values associated to the player
	 */
	public Enum[] getDataValues() {
		return _dataValues;
	}

	/**
	 * gets the team associated to the player 
	 * 
	 * @return The team associated to the player
	 */
	public PlayerTeam getTeam() {
		return _team;
	}
	
	@Override public String toString() {
		return _team.toString();
	}
}
