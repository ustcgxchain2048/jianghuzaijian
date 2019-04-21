#include <graphenelib/asset.h>
#include <graphenelib/global.h>
#include <graphenelib/contract.hpp>
#include <graphenelib/dispatcher.hpp>
#include <graphenelib/multi_index.hpp>
#include <graphenelib/types.h>
#include <string>

using namespace graphene;
using std::string;
#define WAITING 0
#define RUNNING 1
//#define exit 2


class play_2048 : public contract
{
  public:
    play_2048(uint64_t self)
        : contract(self)
        , players(_self, _self)
        , fightpairs(_self, _self)
    {
    }




    /********基本操作********/

    /// @abi action
    void store(uint64_t id, uint64_t state,uint64_t score)
    {
        players.emplace(_self, [&](auto &player) {  //使用_self让合约付钱,使用get_trx_sender()是调用者付钱
            player.id = id;
            //player.name = name;
            player.state = state;
            player.score = score;
        });
    }

    /// @abi action
    void remove(uint64_t id)
    {
        const auto &it = players.find(id);
        if (it != players.end())
            players.erase(it);
    }

    /// @abi action
    void find(uint64_t id)
    {
        auto player_itr = players.find(id);
        if (player_itr == players.end()) {
            print("can not find this player");
        } else {
            print("player.id = ${id}", ("id", player_itr->id));
        }
    }

    //存储对战表
    /// @abi action
    void storepair(uint64_t primarykey, uint64_t id1, uint64_t id2, uint64_t gamestate, uint64_t score1, uint64_t score2)
    {
        fightpairs.emplace(_self, [&](auto &fightpair) {  //使用_self让合约付钱,使用get_trx_sender()是调用者付钱
            fightpair.primarykey = primarykey;
            fightpair.id1 = id1;
            fightpair.id2 = id2;
            fightpair.gamestate = gamestate;
            fightpair.score1 = score1;
            fightpair.score2 = score2;
        });
    }



    /********游戏操作********/

    /// @abi action
    void login(uint64_t id){
        store(id,WAITING,0);
    }

    /// @abi action
    void quit(uint64_t id){
        remove(id);
    }

    //初始化对战表
    /// @abi action
    void initpair(uint64_t id1, uint64_t id2,  uint64_t score1, uint64_t score2)
    {
        uint64_t pair_num=1;
        for(auto it=fightpairs.begin(); it != fightpairs.end();++it){
            pair_num++;  //对战表项目数，用作primarykey,
        }
        storepair(pair_num,id1,id2,RUNNING,score1,score2);
    }

    //删除对战表项
    /// @abi action
    void removepair(uint64_t primarykey)
    {
        const auto &it = fightpairs.find(primarykey);
        if (it != fightpairs.end())
            fightpairs.erase(it);
    }

    //更改对战表中1号的成绩
    /// @abi action
    void up2score_1(uint64_t primarykey,uint64_t score_x)
    {
        auto it = fightpairs.find(primarykey);
        if (it != fightpairs.end()) {
            fightpairs.modify(it, _self, [&score_x](auto &the_fightpair) {
                the_fightpair.score1 = score_x;
            });
            print("fightpair.primarykey = ${primarykey}", ("primarykey", it->primarykey));
        } else {
            print("fightpair not found");
        }
    }

    //更改对战表中状态
    /// @abi action
    void up2state(uint64_t primarykey,uint64_t gamestate)
    {
        auto it = fightpairs.find(primarykey);
        if (it != fightpairs.end()) {
            fightpairs.modify(it, _self, [&gamestate](auto &the_fightpair) {
                the_fightpair.gamestate = gamestate;
            });
            print("fightpair.primarykey = ${primarykey}", ("primarykey", it->primarykey));
        } else {
            print("fightpair not found");
        }
    }

    //更改对战表中2号的分数
    /// @abi action
    void up2score_2(uint64_t primarykey,uint64_t score_x)
    {
        auto it = fightpairs.find(primarykey);
        if (it != fightpairs.end()) {
            fightpairs.modify(it, _self, [&score_x](auto &the_fightpair) {
                the_fightpair.score2 = score_x;
            });
            print("fightpair.primarykey = ${primarykey}", ("primarykey", it->primarykey));
        } else {
            print("fightpair not found");
        }
    }

    //更改单人赛中状态
    /// @abi action
    void updatestate(uint64_t id, uint64_t state1)
    {
        auto it = players.find(id);
        if (it != players.end()) {
            players.modify(it, _self, [&state1](auto &the_player) {
                the_player.state = state1;
            });
            print("player.id = ${id}", ("id", it->id));
        } else {
            print("player not found");
        }
        
        
    }
    
    //更改单人赛中分数
    /// @abi action
    void updatescore(uint64_t id, uint64_t score)
    {
        auto it = players.find(id);
        if (it != players.end()) {
            players.modify(it, _self, [&score](auto &the_player) {
                the_player.score = score;
            });
            print("player.id = ${id}", ("id", it->id));
        } else {
            print("player not found");
        }
        
    }
    
    //给与单人游戏者发放奖励
    /// @abi action
    void bonus(uint64_t id, uint64_t score)
    {
        updatescore(id, score);
        //TODO:score -> amount
        //内置api，提现资产到指定账户
        withdraw_asset(_self, id, 1, 10000);
    }

    //根据双人游戏得分发放奖励
    /// @abi action
    void multibonus(uint64_t primarykey)
    {
        auto it=fightpairs.find(primarykey);
        if ((it->score1) > (it->score2))
            {
                withdraw_asset(_self, it->id1, 1, 10000);
            }else{
                withdraw_asset(_self, it->id2, 1, 10000);
            }
    }


    //检验空闲人员，匹配进行游戏
    /// @abi action
    void checkplayers(uint64_t id)
    {
        //uint64_t player_num=0;
        uint64_t waiting_num=0;
        uint64_t fighter_id;    //对手id
        for(auto it=players.begin(); it != players.end();++it){
            print("helloustc","\n");
            print("ID:",it->id,"\n");
            print("state:",it->state,"\n");
            if (it->state==WAITING)
            {
                fighter_id=it->id;
                waiting_num++;
            }
            //player_num++;

        }
        if(waiting_num==0){
            login(id);
        }else if (waiting_num==1)
        {
            login(id);
            updatestate(id,RUNNING);
            updatestate(fighter_id,RUNNING);
            waiting_num--;
            initpair(id,fighter_id,0,0);  //初始化对战表，主键递增，状态为RUNNING
        }

    }
    
    /// @abi action
    void end2players(uint64_t primarykey, uint64_t id,uint64_t score,uint64_t idflag)
    {
        if (idflag==1)
        {
            up2score_1(primarykey,score);
            up2state(primarykey,WAITING);
            
        }else if(idflag==2)
        {
            up2score_2(primarykey,score);
        }
    }

   private:
    //@abi table player i64
    struct player {
        uint64_t id;
        //string name;
        uint64_t state;
        uint64_t score;

        uint64_t primary_key() const { return id; }

        GRAPHENE_SERIALIZE(player, (id)(state)(score));
    };

    typedef graphene::multi_index<N(player), player> player_index;

    player_index players;

    //@abi table fightpair i64
    struct fightpair {
        uint64_t primarykey;
        uint64_t id1;
        uint64_t id2;
        uint64_t gamestate;
        uint64_t score1;
        uint64_t score2;

        uint64_t primary_key() const { return primarykey; }

        GRAPHENE_SERIALIZE(fightpair, (primarykey)(id1)(id2)(gamestate)(score1)(score2));
    };

    typedef graphene::multi_index<N(fightpair), fightpair> fightpair_index;

    fightpair_index fightpairs;

};
GRAPHENE_ABI(play_2048, (store)(remove)(find)(login)(quit)(updatestate)(updatescore)(bonus)(checkplayers)(removepair)(end2players)(multibonus))