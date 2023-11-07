# COMP2042_CW_hcytl1
# Name - Leong Tin Jet

renamed sceneHeigt to SceneHeight, isGoldStauts to isGoldStatus

replace statement lambda to expression lampda

Menu added, with a start button and a image

set multiple field to final


implement the pause and resume button
change the logic of destory block count to remaining block count,cause the previos logic will have error when
it compare destroyblockcount == block.size, if after pause the game , the block.size is 4, and the destroyblockcount is 
also 4, it will go to nextlevel, which is not what we want