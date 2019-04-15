# visualizer

# exeの入出力

## exeに入力
### 初期化
maxTurn

w

h

エージェント数(N)

h行w列のマップのスコア

### ターンの始まり毎
nowTurn

h行w列の領域情報(0:none, 1:my, 2:rival)

MY_X_i MY_Y_i // N行の自分のエージェントの位置情報(左上を(0, 0))

RIVAL_X_i RIVAL_Y_i // N行の相手のエージェントの位置情報

## exeの出力
N行でエージェントの行動を出力

0~9で方向を指定

移動がw, タイル除去がe, 何もしないはn

# mapのデータ
maxTurn

w

h

h行w列の領域ポイント

エージェント数(N)

自分のエージェントの位置(N行でx, y)

相手のエージェントの位置(N行でx, y)