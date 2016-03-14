Implementation:

Essentially, I used the basic layout that was given to us and I just expanded on it. 
In my implementation I still use GameObj, but I added things like color and opacity to its constructor, 
made my own intersect method that uses areas instead of rectangles around the object. 

Using GameObj, I was able to make classes for all my objects and alter them accordingly. I made a class for the
arrow that is shot, for all the circles, for my power-ups (Clock, ExtraLife, GravArrow), for the timeKeeper, 
for my character, and all the other objects that I ended up using.

In order to shoot the arrow I decided to always have an arrow be drawn under the character, but when the space bar is hit,
an invisible Square moves from that exact x position up until the ceiling. The arrow that is drawn has an end point from where the 
space bar is hit, that extends to the invisible square. In other words, when the invisible square moves, it appears as if the arrow 
is extending all the way to the ceiling. 

I ended up using an arrayList for all the circles. The reason for this is so that I could quickly access the specific circle that was
hit and be able to remove it, and add two new smaller circles from the same position with opposite x velocities so it appears as if the 
original bubble got split into two. I then implemented gravity and had the different sized balls bounce at a specific height. In other 
words, the smallest balls have the smallest constant bounce and the biggest balls have the largest constant bounce. 

I later made a timer of a rectangle whose width depended on how much time had elapsed. 

Then I made an arrayList for the number of Lives that were remaining. I start out by adding 6 lives and printing 6 pictures of the 
character on the top left of the screen. Then I specified that if a bubble intersects with the character or the time runs out, to remove one life.

Then I implemented the score. I gave different sized bubbles a different value and if a bubble is hit, the value of that bubble is added
on to the score.

Then I made power ups:
I made each of them arrayLists in order to keep track of the specific object instance that was collected, and to be able to remove it easily if it 
is collected. I also used Math.random in order to create randomness about when these power-ups should drop from the bubbles. I created an int that 
increases by 1 continuously. I then stated that when this int reached 300, the powerUp should disappear, so I remove it from the arrayList
of that specific power-up. In the time that it is in the field, I managed to consistently subtract from the opacity of the object. So, the power up 
will be more transparent as it is getting closer to disappear. 

Extra Lives:
It is a picture of a heart. If it is collected, I just add 1 to the number of lives and then the number of characters drawn on the top left of the
screen automatically increases by 1.

Clock:
It is a picture of a clock. If collected, the time goes down by a little bit, giving the character a little bit more time. 

Anti-Gravity:
I used a picture of two arrows as the power up. If collected, the gravity shifts and the character gets thrown to the ceiling along with the bubbles. 
I had to create a boolean that was gravOn to be able to make the functionality of the shooting arrow still work from the ceiling. I also had to shift the
pictures of the lives and the timer from the top down to the bottom of the screen. i also had to make it so that if the character got hit by a ball in
transition to the ceiling or to the floor, you would not lose a life. I had to load more pictures of the flipped character as well. The gravity constant
that was used to create the bouncing balls in the first place made it possible to shift. 

After that, I created automatic levels. I made a newGame method that started at level 0. In the reset method, however, the level is not altered. Instead,
it uses the level number to automatically calculate the size and how many bubbles should be placed on that screen for that specific level. To do this, I used
mod to determine how many bubbles should be on the screen for each level. What makes these automatic levels even more interesting is the fact that I created a
new timer that has a delay every time a life is lost or a level is passed. This timer, once the delay has passed, calls reset. This means that the game will
always be running until all lives are lost or the game is won at level 12. I set level 12 to be the final level because it was getting way too difficult.
Essentially, if I lose a life, the game will freeze for a couple moments, tell me how many lives I have left, and then automatically restart the level. If I
beat the level, the game will pause momentarily, tell me what level I am advancing to, and then automatically start the next level. 

After this, I implemented the highScore functionality. Essentially I created a bufferedWriter and a bufferedReader, and if the score that I get is higher than
the score that was previously written to the document that is specified, then I write the new score to the txt file. In the reset function, every time the game
is started up again, this file will be read in order to see what the current highScore is. I had to change the constructor for GameCourt in order to automatically
update the HighScore next to the JButtons. 



