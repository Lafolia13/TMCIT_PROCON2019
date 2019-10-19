# solver

## **重要**

これは開発初期に行った虚無です。本選で使われたのは <u> google 以下のプログラム</u> です。

ここに書いてある内容は何も保証できません。なぜならば書いたのは半年前だからです。

## Tab sizeについて

Tab size = 4で書いているため、プレビューが多少崩れています(多分Googleが危惧してるのこういうやつなんだろうね)。  
GithubのURLの最後に"?ts=4"をつけるとTab sizeが4になります。すごい

## 入出力のフォーマットに関して

Visualiserの方のれどめにもありますが、GameDataにはゲームの初めに一度だけ入力をし、  
TurnDataには各ターン毎に入力をします。

GameDataのフォーマット:  
max_turn  
width height  
agentの数  
point_data

TurnDataのフォーマット:  
now_turn  
tile_data  
ally_agent_position  
rival_agent_position  

出力のフォーマット:  
ally_agent_i_action(方向(数字)、行動(char)) (空白はいれません)

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

## search

main.cpp が入ってます。

もはや何やったか覚えてないのであれなんですけど、 仙台名取の部誌を読みながら書いてた記憶があります。
