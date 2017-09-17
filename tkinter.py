import tkinter as tk
import random

class Application(tk.Frame):
    def __init__(self, master=None):
        tk.Frame.__init__(self, master)
        self.pack()
        self.create_widgets()
        
    def create_widgets(self):        
        self.paintboard = tk.Canvas(self, width=800, height=480)
        self.paintboard.pack()
##        self.paintboard.create_rectangle(0,0,800,600,fill='black')
        self.z = self.paintboard.create_rectangle(0,0,800,640,fill='#aaddbb')
        
        self.castle = tk.PhotoImage(file='castle.gif')
        self.spruces = tk.PhotoImage(file='spruces.gif')
        for i in range(25):
            for j in range(15):
                if random.random() < .02:
                    self.paintboard.create_image(32*i,32*j,anchor='nw',image=self.castle)
                elif random.random() < .3:
                    self.paintboard.create_image(32*i,32*j,anchor='nw',image=self.spruces)

##        self.hi_there = tk.Button(self, text="Hello", command=self.move_rect)
##        self.hi_there.pack(side='bottom')

    def say_hi(self):
        print("hi errybody")

    def move_rect(self):
        self.paintboard.coords(self.z,0,0,32,32)

root = tk.Tk()
app = Application(master=root)
app.master.title('Sample application')
app.mainloop()
