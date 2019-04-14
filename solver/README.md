# solver

## Tab sizeについて

Tab size = 4で書いているため、プレビューが多少崩れています(多分Googleが危惧してるのこういうやつなんだろうね)。  
GithubのURLの最後に"?ts=4"をつけるとTab sizeが4になります。すごい

## 入出力のフォーマットに関して

Visualiserの方のれどめにもありますが、GameDataにはゲームの初めに一度だけ入力をし、  
TurnDataには各ターン毎に入力をします。

GameDataのフォーマット:  
max_turn  
width height  
agentの数(Visualiserと相違ですがIssueを投げてます)  
point_data

TurnDataのフォーマット:  
now_turn
ally_agent_i_position
rival_agent_i_position
tile_data

出力のフォーマット:  
ally_agent_i_action(方向(数字)、行動(char)) (空白をいれるかはIssue)

## base

GameData、TurnData、Positionの定義をしてます。  
GameData: 不変なゲームのデータ  
TurnData: ターン毎に変動するゲームのデータ  
Position: 演算可能な座標データ  
クラス内にアクセスする関数、Positionに関するものはここに書きます。  

## calculation

Pointの定義をしています。
Point: 領域ポイント、タイルポイント、合計を格納  
得点計算系はここに書きます。  

## action

Moveの定義をしています。
Move: エージェントの次の行動を格納   
Moveを使ってbaseにアクセス、改変するのはここに書きます。  