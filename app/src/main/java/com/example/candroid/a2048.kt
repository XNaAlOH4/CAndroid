package com.example.candroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.graphics.drawable.GradientDrawable
import android.view.MotionEvent
import kotlin.math.abs
import kotlin.random.Random
import android.widget.Toast

class a2048 : AppCompatActivity() {
    private lateinit var tmpTxt: TextView
    private var cells : Array<TextView?> = arrayOfNulls(16)
    private var x1: Float = 0.0f
    private var y1: Float = 0.0f
    private var empty: Int = 16
    private var mtGrid: Array<Int> = arrayOf(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15)
    private var grid: Array<Int> = arrayOf(0,0,0,0,
                                            0,0,0,0,
                                            0,0,0,0,
                                            0,0,0,0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a2048)

        for(i in 0..15) {
            cells[i] = findViewById(R.id.cell0+i)
            val bckgnd: GradientDrawable = cells[i]?.getBackground() as GradientDrawable
            bckgnd.setColor(0xffffffaf.toInt())
        }

        genNewTile()

        tmpTxt = findViewById(R.id.temp)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when(e.getAction()) {
            MotionEvent.ACTION_DOWN -> {
                x1 = e.getX()
                y1 = e.getY()
            }

            MotionEvent.ACTION_UP -> {
                val dx: Float = e.getX() - x1
                val dy: Float = e.getY() - y1
                val absx = abs(dx)
                val absy = abs(dy)
                var diff = 0.0f

                if((absx > MIN_DIST) && (absy > MIN_DIST)) {
                    diff = absx - absy
                    if(abs(diff) < MIN_DIFF) return true
                }else {
                    diff = (if(absx > MIN_DIST) 1.0f else 0.0f) - (if(absy > MIN_DIST) 1.0f else 0.0f)
                    if(diff == 0.0f) return true
                }

                //Toast.makeText(this, dx.toString() + "," + dy.toString(), Toast.LENGTH_SHORT).show()

                if(diff > 0) {
                    // do x
                    val dir: Int = if(dx < 0) 1 else -1

                    for(y in 0..3) {
                        val k: Int = y * 4
                        var x: Int = if(dir == 1) 1 else 2
                        var combined = 17

                        while (atEdge(dir, x)) {
                            if (grid[x + k] != 0) {
                                var end: Int = x - dir
                                while (atEdge(-dir, end)) {
                                    if (grid[end+k] != 0) {
                                        if (grid[end+k] == grid[x+k] && combined != (end+k)) {
                                            combined = end+k
                                            end -= dir
                                            grid[x+k] *= 2
                                            mtGrid[empty++] = (x+k)
                                        }
                                        break
                                    }
                                    end -= dir
                                }
                                grid[end + dir+k] = grid[x+k]
                                if (x != (end + dir)) {
                                    grid[x+k] = 0
                                    if (getNdx(end + dir + k, mtGrid, empty) != -1)
                                        mtGrid[getNdx(end + dir + k, mtGrid, empty)] = x + k
                                }
                            }
                            x += dir
                        }
                    }
                }else if (diff < 0) {
                    val dir: Int = if(dy < 0) 4 else -4
                    for(x in 0..3) {
                        var y = if(dy < 0) 1 else 2
                        var k = y*4
                        var combined = 17

                        while(atEdge(dir, y)) {
                            if (grid[k + x] != 0) {
                                var end = k - dir
                                while (atEdge(-dir, end / 4)) {
                                    if (grid[end + x] != 0) {
                                        if (grid[end + x] == grid[k + x] && combined != end) {
                                            combined = end
                                            end -= dir
                                            grid[k + x] *= 2
                                            mtGrid[empty++] = k + x
                                        }
                                        break
                                    }
                                    end -= dir
                                }
                                grid[end + dir + x] = grid[k + x]
                                if (k != (end + dir)) {
                                    grid[k + x] = 0
                                    if (getNdx(end + dir + x, mtGrid, empty) != -1)
                                        mtGrid[getNdx(end + dir + x, mtGrid, empty)] = k + x
                                }
                            }
                            y += dir/4
                            k += dir
                        }
                    }
                }

                genNewTile()

                var str = empty.toString() + ": "
                for(i in 0..empty-1) {
                    str += mtGrid[i].toString() + ", "
                }
                tmpTxt.setText(str)

                if(empty == 0) {
                    Toast.makeText(this, "There is no more empty space", Toast.LENGTH_SHORT).show()
                    return true
                }
            }// When touch lifted
        }
        return true
    }

    private fun genNewTile() {
        val r = Random.nextInt(empty)
        grid[mtGrid[r]] = 2 + (Random.nextInt(4)/3)*2
        System.arraycopy(mtGrid, r+1, mtGrid, r, mtGrid.size-r-1)
        empty--

        // Set the values of the cells
        for(i in 0..15) {
            if(grid[i] != 0)
                cells[i]?.setText(grid[i].toString())
            else
                cells[i]?.setText("")
        }
    }

    private fun getNdx(e: Int, arr: Array<Int>, size: Int): Int {
        for(i in 0..size-1) {
            if(arr[i] == e) return i
        }
        return -1
    }

    private fun atEdge(dir: Int, x: Int): Boolean {
        if(dir > 0) {
            return (x < 4)
        }
        return (x >= 0)
    }

    companion object {
        private var MIN_DIST = 100
        private var MIN_DIFF = 60
    }
}