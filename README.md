# DestinyLoadoutUpdate
Track highest Power level loadout (dependent on currently worn)

Started as a joke, and transcended into a full-blown personal project that would help me practice Java coding
and SQL Queries.

For the most part, this project only really works on my own personal computer, since it does have some reliance on it
in terms of the SQL connection string that is used by the SQLReader class. It also has dependencies on the format of
certain websites. If the HTML data and setup of the websites referenced changes, it will likely also break the
PageDataParser class in here.

My friends joked with me about the fact that I should know some info and be keeping track of their highest level loadouts
in Bungie's Destiny 2 videogame. So I started an SQL Server instance localhost which I could use to update the tables that
held all those numbers. But it got tiresome, having to constantly check in with them about their best loadouts, and even then
I'd have to type out the entire query, or constantly click around an SQL file to change the values, and then run the script. 
Way too much clicking, and cursor positioning; repeat that process for even 5 people, at 3 characters a piece, and it just felt 
like a little too much for me to try to keep up.

So that's why this is here. Now, my friends and I play on PlayStation. My policy is that I will not ask them for their PS account,
that's just a no-go, invasion of privacy and a little too much access to be asking people to trust me with. So I'm just trying to get
the next best thing. 

The high-level idea behind this process is to scout a webpage hosted by Bungie. Don't worry, the pages I look at are completely public domain,
there's no hacking or tinkering to try to break into their servers and find people's loadouts. The other thing is that I'm limited to only seeing
what they currently have equipped, or at least the last known equipped loadout that Bungie has updated onto the website. Take those values, compare
them with the ones in the database, and then update accordingly based on what is higher, in each component.

This program alone probably won't eliminate the need for me to update things manually, as there are definitely unpredictable circumstances
that can come up, but at least I can reduce the whole update process to "Run this .jar on this account on this character".

Special thanks to members of E Q U | N O X for inspiration. See you starside.

This is meant to be used as a command-line interface executable. To run, make sure to specify arguments:
- Option "-acc" or "--accountName" --> specify the name of the player (screen tag)
- One of Option "-T", "-H", "-W" --> specify the Guardian Class to search for linked to this account (Titan, Hunter, Warlock)
- Option "-id" or "--idFilePath" --> specify a path to a text file, relative to the directory in which the jar executable exists, that contains the values for each
  provisioned accountName's characters; each line should be specified using the following template:
      - accountName;;;;guardianClassInFull;;;;accountIDValue;;;;charIDValue
 
Currently, this executable works best with accounts that have a single character of each guardian class associated. However, you can arbitrarily specify a player's duplicate
guardian class characters to be of another guardian class, so long as the accountID and charID still match.

E.g. An account with 2 Titan characters
- Titan1 --> accountName;;;;Titan;;;;accountID;;;;charID
- Titan2 --> accountName;;;;Hunter;;;;accountID;;;;charID

In this case, you will simply need to remember that Titan2 has been specified as a "Hunter" class.
      
Be sure to know the hostname, instanceName (optional), databaseName, of your SQL database as well as an authorized username and password with which to update its data.

