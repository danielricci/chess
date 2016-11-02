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

package views;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.AbstractMap.SimpleEntry;
import java.util.Observable;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import controllers.BoardGameController;
import controllers.PlayerController;
import controllers.TileController;
import factories.ControllerFactory;
import models.GameModel;
import models.PlayerModel;
import models.TileModel;
import models.TileModel.NeighborXPosition;
import models.TileModel.NeighborYPosition;

@SuppressWarnings("serial")
public final class BoardGameView extends BaseView {
	
	private final JPanel _gamePanel = new JPanel(new GridBagLayout());	
	
	public BoardGameView() {
		ControllerFactory.instance().get(PlayerController.class).populatePlayers(this);
	}

	@Override public void update(Observable obs, Object arg) {
		
		super.update(obs, arg);
		
		BoardGameController boardGameController = ControllerFactory.instance().get(BoardGameController.class);
		TileModel tileModel = (TileModel)obs;
		
		for(GameModel.Operation operation : tileModel.getOperations()) {
			switch(operation) {
			case PlayerPieceSelected:
				boardGameController.processTileSelected(tileModel);
				break;
			case PlayerPieceMoveCancel:
				boardGameController.processTileCancel(tileModel);
				break;
			case PlayerPieceMoveAccepted:
				boardGameController.processTileMove(tileModel);
				break;
			case Debugger_KingTiles:
			case Debugger_PlayerTiles:
			case Debugger_TileCoordinates:
			case EmptyTileSelected:
			case HideGuides:
			case Refresh:
			case ShowGuides:
				break;
			case Debugger_HighlightNeighbors:
				break;
			default:
				break;
			}
		}
	};
	
	@Override public void render() {
	
		super.render();

		Color firstColor = new Color(209, 139, 71);		
		Color secondColor = new Color(255, 205, 158);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		PlayerController playerController = ControllerFactory.instance().get(PlayerController.class);
		BoardGameController boardGameController = ControllerFactory.instance().get(BoardGameController.class);

		int boardDimensions = boardGameController.getBoardDimensions();		
		Vector<Vector<TileModel>> tiles = new Vector<>();

		for (int row = 0; row < boardDimensions; ++row) {
			
			PlayerModel player = null;
			if(row == 0 || row == 1) {
				player = playerController.getPlayer(0);
			} else if(row == boardDimensions - 1 || row == boardDimensions) {
				player = playerController.getPlayer(1);
			}
			
			// TODO - we should not be doing this amount of work within a view!
			// TODO - move this to the controller!
			Vector<TileModel> tilesRow = new Vector<>();
			for (int col = 0, colorOffset = row % 2;  col < boardDimensions; ++col, colorOffset = (++colorOffset % 2 == 0 ? 0 : 1)) {

				// Set our grid-bad-constraints and create the game tile
				gbc.gridx = col;
				gbc.gridy = row;
				
				// Create the tile and populate its contents
				TileView view = new TileView(colorOffset == 0 ? firstColor : secondColor);
				TileModel tile = boardGameController.populateTile(player, row == 0 || row == boardDimensions - 1, view, this);
				
				// Set the controller of the tile and render it
				view.setController(new TileController(tile));
				view.render();
				
				// Add our components to our view
				_gamePanel.add(view, gbc);			
				tilesRow.add(tile);
			}
			
			// TODO - we should put this somewhere else?
			// TODO - we need to remove the dependency of models, we should only really be calling controllers!
			// Populate the row neighbors
			for(int i = 0; i < tilesRow.size(); ++i)
			{
				TileModel tile = tilesRow.get(i);
				tile.setNeighbors(
					NeighborYPosition.NEUTRAL,
					new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.LEFT, i - 1 < 0 ? null : tilesRow.get(i - 1)),
					new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.NEUTRAL, tile),
					new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.RIGHT, i + 1 == tilesRow.size() ? null : tilesRow.get(i + 1))
				);
			}
		
			// As long as we have one row in our buffer, try and connect it to what has currently
			// been generated
			// TODO - can we parallelize this?
			if(tiles.size() > 1) {

				// Grab the most recently populated two elements to populate their neighbors
				Vector<TileModel> firstRow = tiles.get(tiles.size() - 2);
				Vector<TileModel> secondRow = tiles.lastElement();
				
				// Attempts to get the row before the first row being selected
				Vector<TileModel> previousFirstRow = tiles.size() > 2 ? tiles.get(tiles.indexOf(firstRow) - 1) : null;
				
				// Populate both rows neighbors
				for(int i = 0; i < firstRow.size(); ++i) {
					TileModel firstRowElement = firstRow.get(i);
					TileModel secondRowElement = secondRow.get(i);
					
					// Set the neighbors of the first rows top neighbors
					firstRowElement.setNeighbors(NeighborYPosition.TOP, 
						new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.LEFT, previousFirstRow != null && i > 0 ? previousFirstRow.get(i - 1) : null),
						new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.NEUTRAL, previousFirstRow != null ? previousFirstRow.get(i) : null),
						new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.RIGHT, previousFirstRow != null && i + 1 < firstRow.size() ? previousFirstRow.get(i + 1) : null)
					);
					
					// Set the neighbors of the first rows bottom neighbors
					firstRowElement.setNeighbors(NeighborYPosition.BOTTOM, 
						new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.LEFT, i > 0 ? secondRow.get(i - 1) : null),
						new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.NEUTRAL, secondRowElement),
						new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.RIGHT, i + 1 < firstRow.size() ? secondRow.get(i + 1) : null)
					);
					
					// Set the neighbors of the second rows top neighbors
					secondRowElement.setNeighbors(NeighborYPosition.TOP, 
						new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.LEFT, i > 0 ? firstRow.get(i - 1) : null),
						new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.NEUTRAL, firstRowElement),
						new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.RIGHT, i + 1 < firstRow.size() ? firstRow.get(i + 1) : null)
					);
					
					// The bottom neighbors of every second row is initially set to null because the neighbors are not yet known
					secondRowElement.setNeighbors(NeighborYPosition.BOTTOM,
						new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.LEFT, null),
						new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.NEUTRAL, null),
						new SimpleEntry<NeighborXPosition, TileModel>(NeighborXPosition.RIGHT, null)
					);				
				}
			}
			tiles.add(tilesRow);
		}
		add(_gamePanel);
	}


	@Override public void destroy() {
		_gamePanel.removeAll();
	}
}